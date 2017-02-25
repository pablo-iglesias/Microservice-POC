package adapter.response.api.json;

public class ApiResponseError extends ApiResponse{

	private String error;
	
	public ApiResponseError(String error){
		this.setError(error);
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
