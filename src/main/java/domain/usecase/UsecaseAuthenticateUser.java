package domain.usecase;

import domain.constraints.repository.IUserRepository;

import domain.entity.User;

import javax.inject.Inject;

public class UsecaseAuthenticateUser extends Usecase {

    public enum Result {
        USER_AUTHENTICATED_SUCCESSFULLY,
        DID_NOT_AUTHENTICATE
    }

    private @Inject IUserRepository userRepository;
    private String username = null;
    private String password = null;
    private Integer refUserId = null;

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

        this.password = password;
    }

    public Integer getRefUserId() {
        return refUserId;
    }

    @Override
    public Result execute() throws Exception {

        if (username == null) {
            throw new IllegalStateException("username not provided");
        }
        else if (password == null) {
            throw new IllegalStateException("password not provided");
        }
        else {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);

            if(userRepository.findUser(user)) {
                refUserId = user.getId();
                return Result.USER_AUTHENTICATED_SUCCESSFULLY;
            }
            else {
                return Result.DID_NOT_AUTHENTICATE;
            }
        }
    }
}