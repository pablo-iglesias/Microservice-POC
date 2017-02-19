package adapter.response;

import java.util.Map;

public class ResponseOK extends ApplicationResponse{

	public ResponseOK() {
		super(RESPONSE_OK);
	}
	
	public ResponseOK(String view, Map<String, String> data) {
		super(RESPONSE_OK, view, data);
	}
}
