package core.entity;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private int code;
    private Map<String, String> headers;
    private String body;

    public HttpResponse(int code) {
        this.code = code;
        body = "";
        headers = new HashMap<String, String>();
    }

    public HttpResponse(int code, String body) {
        this.code = code;
        this.body = body;
        headers = new HashMap<String, String>();
    }

    public int getCode() {
        return code;
    }

    public String getBody() {
        return body;
    }

    public String getHeader(String header) {
        return headers.get(header);
    }

    public void setHeader(String header, String value) {
        headers.put(header, value);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
