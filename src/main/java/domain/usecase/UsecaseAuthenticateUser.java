package domain.usecase;

import domain.entity.User;
import domain.entity.factory.UserFactory;

import adapter.repository.UserRepository;
import adapter.repository.RoleRepository;

public class UsecaseAuthenticateUser extends Usecase {

    public static final int RESULT_USER_AUTHENTICATED_SUCCESSFULLY = 1;
    public static final int RESULT_DID_NOT_AUTHENTICATE = 2;

    // Factory
    private UserFactory factory;

    // Input data
    private String username = null;
    private String password = null;

    // Output data
    private Integer refUserId = null;

    // Getter & Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("username cannot be null");
        }

        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("password cannot be null");
        }

        this.password = password;
    }

    public Integer getRefUserId() {
        return refUserId;
    }

    // Constructor
    public UsecaseAuthenticateUser(UserRepository userRepository, RoleRepository roleRepository) throws Exception {

        factory = new UserFactory(userRepository, roleRepository);
    }

    public UsecaseAuthenticateUser(UserFactory factory) {

        this.factory = factory;
    }

    // Business Logic
    public int execute() throws Exception {

        if (username == null) {
            throw new IllegalStateException("username not provided");
        }

        if (password == null) {
            throw new IllegalStateException("password not provided");
        }

        User user = factory.create(username, password);

        if (user != null) {
            refUserId = user.getId();
            return RESULT_USER_AUTHENTICATED_SUCCESSFULLY;
        }

        return RESULT_DID_NOT_AUTHENTICATE;
    }
}
