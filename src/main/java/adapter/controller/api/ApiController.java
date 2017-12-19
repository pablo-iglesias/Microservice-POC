package adapter.controller.api;

import java.net.HttpURLConnection;
import java.util.Base64;
import java.util.Map;
import java.util.regex.Pattern;

import core.Helper;
import core.entity.HttpRequest;
import core.entity.HttpResponse;

import adapter.controller.Controller;

import adapter.response.model.api.ApiResponse;
import adapter.response.model.api.ApiResponseError;

/**
 * Base controller for the REST API
 */
abstract public class ApiController extends Controller {

    private static final String[] SUPPORTED_MEDIA_TYPES = {
            "*/*",
            "application/*",
            "application/json",
            "application/xml"
    };

    /**
     * Perform Basic Authentication check
     * 
     * @param request
     * @return
     * @throws Exception
     */
    private Integer authenticate(HttpRequest request) throws Exception
    {
        String auth = request.getHeaders().getFirst("Authorization");

        if (auth != null) {
            auth = Helper.match(auth, "Basic (.+)");

            if (auth != null) {
                auth = new String(Base64.getDecoder().decode(auth));
                Map<String, String> credentials = Helper.group(auth, "(?<username>[^:]+):(?<password>.+)");

                if (credentials != null && !credentials.isEmpty()) {
                    Integer uid = authenticate(credentials.get("username"), credentials.get("password"));

                    if (uid != null) {
                        return uid;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Creates and returns final HttpResponse object, used only for those responses that deliver actual
     * content, handles content negotiation
     *
     * @param request
     * @param httpCode
     * @param response
     * @return
     * @throws Exception
     */
    protected HttpResponse getResponse(HttpRequest request, int httpCode, ApiResponse response) throws Exception
    {
        String accept = request.getHeaders().getFirst("Accept");

        if (accept == null) {
            accept = SUPPORTED_MEDIA_TYPES[0];
        }

        double highest = 0;
        String type = "";

        for (String mediaType : SUPPORTED_MEDIA_TYPES) {
            Map<String, String> match = Helper.group(accept, Pattern.quote(mediaType) + "(;q=(?<quality>[0-9.]*))?");

            if (match != null) {
                double quality;
                if (match.containsKey("quality") && match.get("quality") != null) {
                    quality = Double.valueOf(match.get("quality"));
                } else {
                    quality = 1d;
                }

                if (quality > highest) {
                    highest = quality;
                    type = mediaType;
                }

                if (quality >= 1)
                    break;
            }
        }

        HttpResponse httpResponse;

        switch (type) {
            case "*/*":
            case "application/*":
            case "application/json":
                httpResponse = new HttpResponse(httpCode, response.getJson());
                httpResponse.setHeader("Content-Type", "application/json");
                return httpResponse;
            case "application/xml":
                httpResponse = new HttpResponse(httpCode, response.getXml());
                httpResponse.setHeader("Content-Type", "application/xml");
                return httpResponse;
        }

        return new HttpResponse(HttpURLConnection.HTTP_NOT_ACCEPTABLE);
    }

    /**
     * Handler for Api requests, diverts requests to appropriate method of the controller
     * 
     * @param request
     * @return
     * @throws Exception
     */
    public HttpResponse handler(HttpRequest request) throws Exception
    {
        // Get id of authenticated user
        Integer authUserId = authenticate(request);

        if (authUserId != null) {

            // Requests that refer an specific user id in the URI
            if (request.contains("id") && request.get("id") != null) {

                // Resource id
                Integer resId;

                // Get id of referenced user
                try {
                    resId = new Integer(request.get("id"));
                } catch (NumberFormatException e) {

                    return getResponse(
                        request,
                        HttpURLConnection.HTTP_BAD_REQUEST,
                        new ApiResponseError("Invalid resource identifier format, integer expected")
                    );
                }

                switch (request.getMethod()) {
                    case "GET":
                        return GET(request, resId);
                    case "PUT":
                        return PUT(request, authUserId, resId, request.getBody());
                    case "DELETE":
                        return DELETE(request, authUserId, resId);
                }
            }
            // Request that refer the entire collection of users
            else {
                switch (request.getMethod()) {
                    case "GET":
                        return GET(request);
                    case "POST":
                        return POST(request, authUserId, request.getBody());
                }
            }
        } else {
            return new HttpResponse(HttpURLConnection.HTTP_UNAUTHORIZED);
        }

        return getResponse(
            request,
            HttpURLConnection.HTTP_BAD_REQUEST,
            new ApiResponseError("The requested action is not supported by the specified resource")
        );
    }

    protected abstract HttpResponse GET(HttpRequest request) throws Exception;
    protected abstract HttpResponse GET(HttpRequest request, Integer resId) throws Exception;
    protected abstract HttpResponse POST(HttpRequest request, Integer authUserId, String body) throws Exception;
    protected abstract HttpResponse PUT(HttpRequest request, Integer authUserId, Integer resId, String body) throws Exception;
    protected abstract HttpResponse DELETE(HttpRequest request, Integer authUserId, Integer resId) throws Exception;
}
