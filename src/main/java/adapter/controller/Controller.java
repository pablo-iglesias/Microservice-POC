package adapter.controller;

import java.util.Map;

import core.Helper;

import adapter.repository.RoleRepository;
import adapter.repository.UserRepository;
import adapter.repository.factory.RepositoryFactory;

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

        RepositoryFactory factory = new RepositoryFactory();
        UserRepository userModel = (UserRepository) factory.create("User");
        RoleRepository roleModel = (RoleRepository) factory.create("Role");

        UsecaseAuthenticateUser usecase = new UsecaseAuthenticateUser(userModel, roleModel);
        usecase.setUsername(username);
        usecase.setPassword(password);

        switch(usecase.execute()) {
            case UsecaseAuthenticateUser.RESULT_USER_AUTHENTICATED_SUCCESSFULLY:
                return new Integer(usecase.getRefUserId());

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
