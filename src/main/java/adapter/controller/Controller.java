package adapter.controller;

import java.util.Map;

import adapter.repository.UserRepository;
import core.Helper;

import domain.usecase.UsecaseAuthenticateUser;

public class Controller {

    /**
     * The authenticate method, rather than being itself a page or rest endpoint
     * handler, is used by those
     * 
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    protected static Integer authenticate(String username, String password) throws Exception {

        UsecaseAuthenticateUser usecase = new UsecaseAuthenticateUser(new UserRepository());
        usecase.setUsername(username);
        usecase.setPassword(password);

        switch(usecase.execute()) {
            case UsecaseAuthenticateUser.RESULT_USER_AUTHENTICATED_SUCCESSFULLY:
                return usecase.getRefUserId();

            case UsecaseAuthenticateUser.RESULT_DID_NOT_AUTHENTICATE:
            default:
                return null;
        }
    }

    /**
     * Parses a POST or GET query string and returns its parameters as a
     * key-value map
     * 
     * @return
     */
    protected static Map<String, String> parseQueryString(String queryString) {

        return Helper.map(queryString, "&*([^=]+)=([^&]+)");
    }
}
