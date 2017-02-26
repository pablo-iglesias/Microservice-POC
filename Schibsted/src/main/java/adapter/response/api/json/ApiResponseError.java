package adapter.response.api.json;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ApiResponseError extends ApiResponse{

	public String getXml() throws Exception{
		
        return ApiResponse.getXml(this);
	}
	
	private String error;
	
	public ApiResponseError(){
		error = "";
	}
	
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
