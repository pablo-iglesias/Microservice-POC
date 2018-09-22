package domain.usecase.api;

import domain.constraints.RoleObject;
import domain.constraints.UserObject;

import domain.constraints.repository.IRoleRepository;
import domain.constraints.repository.IUserRepository;

import domain.usecase.Usecase;

import domain.entity.Role;
import domain.entity.User;

import javax.inject.Inject;

public class UsecaseGetOneUser extends Usecase {

    public enum Result {
        USER_RETRIEVED_SUCCESSFULLY,
        USER_NOT_FOUND
    }

    private @Inject IUserRepository userRepository;
    private @Inject IRoleRepository roleRepository;
    private Integer refUserId = null;
    private User user = null;
    private Role[] roles = null;

    public void setRefUserId(Integer refUserId) {
        if (refUserId == null) {
            throw new IllegalArgumentException("refUserId cannot be null");
        }

        this.refUserId = refUserId;
    }

    public UserObject getUser() {
        return user;
    }

    public RoleObject[] getRoles() {
        return roles;
    }

    @Override
    public Result execute() throws Exception {

        if (refUserId == null) {
            throw new IllegalStateException("refUserId not provided");
        }
        else{
            user = new User(refUserId);

            if (userRepository.findUser(user)) {
                roles = roleRepository.getRolesByUser(user);
                return Result.USER_RETRIEVED_SUCCESSFULLY;
            }
            else {
                return Result.USER_NOT_FOUND;
            }
        }
    }
}