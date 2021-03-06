package adapter.controller.application;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

import core.Server;
import core.entity.HttpRequest;
import core.entity.Session;

import adapter.controller.Controller;

import adapter.response.model.application.ApplicationResponse;

import domain.usecase.application.UsecasePage;
import domain.usecase.application.UsecaseWelcome;

/**
 * Controller for the HTML based application
 */
public class ApplicationController extends Controller {

    /**
     * Default page, displays login form, if already has a session, redirects to welcome page
     * 
     * @param request
     * @param session
     * @return
     */
    public ApplicationResponse index(HttpRequest request, Session session) {
        if (session != null) {
            return new ApplicationResponse()
                .setResponseCode(ApplicationResponse.RESPONSE_REDIRECT)
                .setLocation("/welcome")
                .setSession(session);
        }

        // If the application has a page number in the query string, it will be sent to the template system
        // This has something to do with the feature of redirecting user to last attempted page on login
        if (request.get("query") != null && !request.get("query").equals("")) {
            Map<String, String> segments = parseQueryString(request.get("query"));
            if (segments.containsKey("page")) {
                Map<String, Object> data = new HashMap<>();
                data.put("page", segments.get("page"));
                return new ApplicationResponse()
                    .setResponseCode(ApplicationResponse.RESPONSE_OK)
                    .setView(ApplicationResponse.TEMPLATE_LOGIN)
                    .setData(data);
            }
        }

        return new ApplicationResponse()
            .setResponseCode(ApplicationResponse.RESPONSE_OK)
            .setView(ApplicationResponse.TEMPLATE_LOGIN);
    }

    /**
     * Login endpoint, receives user data by POST, authenticates user, creates
     * session, redirects to recorded page or to welcome page
     * 
     * @param request
     * @param session
     * @return
     * @throws Exception
     */
    public ApplicationResponse login(HttpRequest request, Session session) throws Exception {

        if (request.getMethod().matches("POST") && session == null)
        {
            // Parse payload of the post for parameters
            Map<String, String> params = parseQueryString(request.getBody());

            if (params.containsKey("username") && params.containsKey("password")) {
                Integer refUserId = authenticate(params.get("username"), params.get("password"));

                if (refUserId != null) {
                    session = Server.createSession(refUserId);

                    if (params.containsKey("page")) {
                        UsecasePage usecasePage = Server.getInstance(UsecasePage.class);
                        usecasePage.setRefUserId(refUserId);
                        usecasePage.setPage(params.get("page") == null ? null : Integer.parseInt(params.get("page")));

                        switch(usecasePage.execute())
                        {
                            case PAGE_RETRIEVED_SUCCESSFULLY:
                                return new ApplicationResponse()
                                    .setResponseCode(ApplicationResponse.RESPONSE_REDIRECT)
                                    .setLocation("/page_" + params.get("page"))
                                    .setSession(session);

                            case PAGE_NOT_ALLOWED:
                            default:
                                break;
                        }
                    }

                    return new ApplicationResponse()
                        .setResponseCode(ApplicationResponse.RESPONSE_REDIRECT)
                        .setLocation("/welcome")
                        .setSession(session);
                }
            }

            if (params.containsKey("page")) {
                return new ApplicationResponse()
                    .setResponseCode(ApplicationResponse.RESPONSE_REDIRECT)
                    .setLocation("/?page=" + params.get("page"));
            }
        }

        return new ApplicationResponse()
                .setResponseCode(ApplicationResponse.RESPONSE_REDIRECT)
                .setLocation("/");
    }

    /**
     * Logs the user out, redirects to index page
     * 
     * @param request
     * @param session
     * @return
     * @throws Exception
     */
    public ApplicationResponse logout(HttpRequest request, Session session) {

        if (session != null) {
            Server.removeSession(session.getSessionToken());
        }

        return new ApplicationResponse()
            .setResponseCode(ApplicationResponse.RESPONSE_REDIRECT)
            .setLocation("/");
    }

    /**
     * Welcome page, displays user name, list of roles and page links, and
     * logout button
     * 
     * @param request
     * @param session
     * @return
     * @throws Exception
     */
    public ApplicationResponse welcome(HttpRequest request, Session session) throws Exception {

        if (session != null)
        {
            UsecaseWelcome usecase = Server.getInstance(UsecaseWelcome.class);
            usecase.setRefUserId(session.getUserId());

            switch(usecase.execute())
            {
                case USER_RETRIEVED_SUCCESSFULLY:
                    Map<String, Object> data = new HashMap<>();
                    data.put("user_name", usecase.getUsername());

                    if (usecase.getRoles() != null) {
                        Map<String, String> roles = new HashMap<>();
                        Arrays.stream(usecase.getRoles()).forEach((role)-> roles.put(role.getName(), role.getPage()));
                        data.put("roles", roles);
                    }

                    return new ApplicationResponse()
                        .setResponseCode(ApplicationResponse.RESPONSE_OK)
                        .setView(ApplicationResponse.TEMPLATE_WELCOME)
                        .setData(data);

                case USER_NOT_FOUND:
                default:
                    Server.removeSession(session.getSessionToken());
                    return new ApplicationResponse()
                        .setResponseCode(ApplicationResponse.RESPONSE_REDIRECT)
                        .setLocation("/");

            }
        }

        return new ApplicationResponse()
            .setResponseCode(ApplicationResponse.RESPONSE_REDIRECT)
            .setLocation("/");
    }

    /**
     * Standard page, displays user name and logout button
     * 
     * @param request
     * @param session
     * @return
     */
    public ApplicationResponse page(HttpRequest request, Session session) throws Exception {

        if (session != null && request.contains("page")) {

            UsecasePage usecase = Server.getInstance(UsecasePage.class);
            usecase.setRefUserId(session.getUserId());
            usecase.setPage(request.get("page") == null ? null : Integer.parseInt(request.get("page")));

            switch(usecase.execute())
            {
                case PAGE_RETRIEVED_SUCCESSFULLY:
                    Map<String, Object> data = new HashMap<>();
                    data.put("page", request.get("page"));
                    data.put("user_name", usecase.getUsername());
                    return new ApplicationResponse()
                        .setResponseCode(ApplicationResponse.RESPONSE_OK)
                        .setView(ApplicationResponse.TEMPLATE_PAGE)
                        .setData(data);

                case PAGE_NOT_ALLOWED:
                default:
                    return new ApplicationResponse().setResponseCode(ApplicationResponse.RESPONSE_DENIED);

            }
        }

        if(request.contains("page")) {
            return new ApplicationResponse()
                .setResponseCode(ApplicationResponse.RESPONSE_REDIRECT)
                .setLocation("/?page=" + request.get("page"));
        }

        return new ApplicationResponse()
            .setResponseCode(ApplicationResponse.RESPONSE_REDIRECT)
            .setLocation("/");
    }
}
