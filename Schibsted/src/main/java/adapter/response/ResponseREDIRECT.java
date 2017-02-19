package adapter.response;

import core.entity.Session;

public class ResponseREDIRECT extends ApplicationResponse{

	public ResponseREDIRECT(String location) {
		super(RESPONSE_REDIRECT);
		setLocation(location);
	}
	
	public ResponseREDIRECT(String location, Session session) {
		super(RESPONSE_REDIRECT);
		setLocation(location);
		setSession(session);
	}	
}
