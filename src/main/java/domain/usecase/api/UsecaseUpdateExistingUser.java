package domain.usecase.api;

import domain.constraints.UserObject;

import domain.constraints.repository.IRoleRepository;
import domain.constraints.repository.IUserRepository;

import domain.entity.User;

import domain.usecase.Usecase;
import domain.service.UserService;

import javax.inject.Inject;

public class UsecaseUpdateExistingUser extends Usecase{

    public enum Result {
        USER_UPDATED_SUCCESSFULLY,
        NOT_AUTHORISED,
        USER_DOES_NOT_EXIST,
        USERNAME_ALREADY_TAKEN,
        USER_NOT_UPDATED,
        BAD_INPUT_DATA
    }

    private @Inject UserService service;
    private @Inject IUserRepository userRepository;
    private @Inject IRoleRepository roleRepository;
    private User user = null;
    private Integer authUserId = null;
    private Integer refUserId = null;
    private User userData = null;

    public void setAuthUserId(Integer authUserId) {
        if (authUserId == null) {
            throw new IllegalArgumentException("authUserId cannot be null");
        }

        this.authUserId = authUserId;
    }

    public void setRefUserId(Integer refUserId) {
        if (refUserId == null) {
            throw new IllegalArgumentException("refUserId cannot be null");
        }

        this.refUserId = refUserId;
    }
    
    public void setUserData(UserObject userData) {
        if (userData == null) {
            throw new IllegalArgumentException("userData cannot be null");
        }

        this.userData = new User(userData);
    }

    @Override
    public Result execute() throws Exception {

        if (authUserId == null){
            throw new IllegalStateException("authUserId not provided");
        }

        if (refUserId == null){
            throw new IllegalStateException("refUserId not provided");
        }

        if (userData == null){
            throw new IllegalStateException("userData not provided");
        }

        if (!userData.containsValidData()) {
            return Result.BAD_INPUT_DATA;
        }

        if (!service.isUserAnAdmin(new User(authUserId))) {
            return Result.NOT_AUTHORISED;
        }

        if (!userRepository.findUser(new User(refUserId))) {
            return Result.USER_DOES_NOT_EXIST;
        }
        else {
            userData.setId(refUserId);
            User nameHolder = new User().setUsername(userData.getUsername());
            if (userRepository.findUser(nameHolder) && !userData.sameIdAs(nameHolder)) {
                return Result.USERNAME_ALREADY_TAKEN;
            }
            else if (!userRepository.updateUser(userData)) {
                return Result.USER_NOT_UPDATED;
            }

            return Result.USER_UPDATED_SUCCESSFULLY;
        }
    }
}
