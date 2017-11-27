package domain.usecase;

import adapter.repository.UserRepository;
import domain.Helper;
import domain.entity.User;

import domain.constraints.repository.IUserRepository;

public class UsecaseAuthenticateUser extends Usecase {

    public static final int RESULT_USER_AUTHENTICATED_SUCCESSFULLY = 1;
    public static final int RESULT_DID_NOT_AUTHENTICATE = 2;

    // Repos
    private IUserRepository userRepository;

    // Input data
    private String username = null;
    private String password = null;

    public void setUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("username cannot be null");
        }

        this.username = username;
    }

    public void setPassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("password cannot be null");
        }

        this.password = Helper.SHA1(password);
    }

    // Output data
    private Integer refUserId = null;

    public Integer getRefUserId() {
        return refUserId;
    }

    // Constructor
    public UsecaseAuthenticateUser(IUserRepository userRepository) throws Exception {
        this.userRepository = userRepository;
    }

    // Business Logic
    public int execute() throws Exception {

        if (username == null) {
            throw new IllegalStateException("username not provided");
        }
        else if (password == null) {
            throw new IllegalStateException("password not provided");
        }
        else {
            User user = userRepository.getUser(username, password);

            if (user != null) {
                refUserId = user.getId();
                return RESULT_USER_AUTHENTICATED_SUCCESSFULLY;
            }
            else {
                return RESULT_DID_NOT_AUTHENTICATE;
            }
        }
    }
}