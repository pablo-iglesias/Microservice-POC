package adapter.response;

public class ApplicationResponseILEGAL extends ApplicationResponse{

	public ApplicationResponseILEGAL() {
		super(RESPONSE_ILEGAL);
	}
	
	/**
	 * This constructor is used when the user attempted to go to a private page and we want to redirect to it after login
	 * 
	 * @param page
	 */
	public ApplicationResponseILEGAL(String page) {
		super(RESPONSE_ILEGAL);
		setStart(page);
	}
}
