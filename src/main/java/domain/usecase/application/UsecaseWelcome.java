package domain.usecase.application;

import domain.constraints.repository.IRoleRepository;
import domain.constraints.repository.IUserRepository;

import domain.usecase.Usecase;

import domain.entity.User;
import domain.entity.Role;

import domain.constraints.RoleObject;

import javax.inject.Inject;

public class UsecaseWelcome extends Usecase {

    public enum Result {
        USER_RETRIEVED_SUCCESSFULLY,
        USER_NOT_FOUND
    }

    private @Inject IUserRepository userRepository;
    private @Inject IRoleRepository roleRepository;
    private Integer refUserId = null;
    private String username = null;
    private Role[] roles = null;

    public void setRefUserId(Integer refUserId) {
        if (refUserId == null) {
            throw new IllegalArgumentException("refUserId cannot be null");
        }

        this.refUserId = refUserId;
    }

    public String getUsername() {
        return username;
    }

    public RoleObject[] getRoles() {
        return roles;
    }

    @Override
    public Result execute() throws Exception {

        if (refUserId == null) {
            throw new IllegalStateException("refUserId not provided");
        }
        else {

            User user = new User(refUserId);

            if (userRepository.findUser(user)) {
                username = user.getUsername();
                roles = roleRepository.getRolesByUser(user);
                return Result.USER_RETRIEVED_SUCCESSFULLY;
            }

            return Result.USER_NOT_FOUND;
        }
    }
}
