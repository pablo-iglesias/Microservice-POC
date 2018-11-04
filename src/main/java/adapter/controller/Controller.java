package adapter.controller;

import java.util.Map;

import core.Helper;

import core.Server;
import domain.usecase.UsecaseAuthenticateUser;

abstract public class Controller {

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

        UsecaseAuthenticateUser usecase = Server.getInstance(UsecaseAuthenticateUser.class);
        usecase.setUsername(username);
        usecase.setPassword(password);

        switch(usecase.execute()) {
            case USER_AUTHENTICATED_SUCCESSFULLY:
                return usecase.getRefUserId();

            case DID_NOT_AUTHENTICATE:
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
