package adapter.response;

import core.entity.Session;

public class ApplicationResponseREDIRECT extends ApplicationResponse{

	public ApplicationResponseREDIRECT(String location) {
		super(RESPONSE_REDIRECT);
		setLocation(location);
	}
	
	public ApplicationResponseREDIRECT(String location, Session session) {
		super(RESPONSE_REDIRECT);
		setLocation(location);
		setSession(session);
	}	
}
