package adapter.response.model.api;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ApiResponse")
public class ApiResponseError extends ApiResponse {

    private String error;

    public ApiResponseError(String error) {
        this.setError(error);
    }

    public void setError(String error) {
        this.error = error;
    }
}
