package domain.usecase.api;

import domain.constraints.UserObject;

import domain.constraints.repository.IRoleRepository;
import domain.constraints.repository.IUserRepository;

import domain.entity.User;
import domain.service.UserService;
import domain.usecase.Usecase;

import javax.inject.Inject;

public class UsecaseAddNewUser extends Usecase {

    public enum Result {
        USER_CREATED_SUCCESSFULLY,
        NOT_AUTHORISED,
        USER_ALREADY_EXISTS,
        USER_NOT_CREATED,
        BAD_INPUT_DATA
    }

    private @Inject UserService service;
    private @Inject IUserRepository userRepository;
    private @Inject IRoleRepository roleRepository;
    private User authUser = null;
    private User newUser = null;

    public void setAuthUserId(Integer authUserId) {
        if (authUserId == null) {
            throw new IllegalArgumentException("authUserId cannot be null");
        }

        authUser = new User(authUserId);
    }

    public void setUserData(UserObject userData) {
        if (userData == null) {
            throw new IllegalArgumentException("userData cannot be null");
        }

        newUser = new User(userData);
    }

    @Override
    public Result execute() throws Exception {

        if (authUser == null) {
            throw new IllegalStateException("authUserId not provided");
        }

        if (newUser == null) {
            throw new IllegalStateException("userData not provided");
        }

        if (!newUser.containsValidData()) {
            return Result.BAD_INPUT_DATA;
        }

        if (!service.isUserAnAdmin(authUser)) {
            return Result.NOT_AUTHORISED;
        }

        if (userRepository.findUser(newUser)) {
            return Result.USER_ALREADY_EXISTS;
        }

        if (service.createNewUser(newUser)) {
            return Result.USER_CREATED_SUCCESSFULLY;
        }

        return Result.USER_NOT_CREATED;
    }
}
