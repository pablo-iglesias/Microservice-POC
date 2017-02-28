package adapter.response.api;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ApiResponse")
public class ApiResponseError extends ApiResponse {

    public String getXml() throws Exception {

        return ApiResponse.getXml(this);
    }

    private String error;

    public ApiResponseError() {
        error = "";
    }

    public ApiResponseError(String error) {
        this.setError(error);
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
