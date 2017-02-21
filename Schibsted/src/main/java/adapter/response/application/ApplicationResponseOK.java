package adapter.response.application;

import java.util.Map;

public class ApplicationResponseOK extends ApplicationResponse{

	public ApplicationResponseOK() {
		super(RESPONSE_OK);
	}
	
	public ApplicationResponseOK(String view, Map<String, Object> data) {
		super(RESPONSE_OK, view, data);
	}
}
