package adapter.response.api.json;

import com.google.gson.Gson;

public class ApiResponse {

	public String getJson(){
		return new Gson().toJson(this);
	}
	
}
