package adapter.response.model.application;

import java.util.Map;

import core.entity.Session;

/**
 * This class is a value object for the standard response of the application
 * controller to the request handler
 * 
 * @author Peibol
 */
public class ApplicationResponse {

    // Response codes
    public static final int RESPONSE_DENIED = -1;
    public static final int RESPONSE_OK = 0;
    public static final int RESPONSE_REDIRECT = 1;

    // HTML Templates
    public static final String TEMPLATE_LOGIN = "templates/login.html";
    public static final String TEMPLATE_WELCOME = "templates/welcome.html";
    public static final String TEMPLATE_PAGE = "templates/page.html";

    private int response; // Application response code
    private Session session = null; // New session created by the Application
    private String location = null; // Redirect to this location
    private String view = null; // Template to render
    private Map<String, Object> data = null; // Data to populate the template

    public ApplicationResponse() {

    }

    public ApplicationResponse(int response) {
        setResponseCode(response);
    }

    public int getResponseCode() {
        return response;
    }

    public ApplicationResponse setResponseCode(int responseCode) {
        this.response = responseCode;
        return this;
    }

    public String getView() {
        return view;
    }

    public ApplicationResponse setView(String view) {
        this.view = view;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public ApplicationResponse setData(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public ApplicationResponse setLocation(String location) {
        this.location = location;
        return this;
    }

    public Session getSession() {
        return session;
    }

    public ApplicationResponse setSession(Session session) {
        this.session = session;
        return this;
    }
}
