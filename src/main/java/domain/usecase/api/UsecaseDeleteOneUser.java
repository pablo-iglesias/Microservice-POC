package domain.usecase.api;

import domain.contract.repository.IRoleRepository;
import domain.contract.repository.IUserRepository;

import domain.entity.User;
import domain.service.UserService;
import domain.usecase.Usecase;

import javax.inject.Inject;

public class UsecaseDeleteOneUser extends Usecase{

    public enum Result {
        USER_DELETED_SUCCESSFULLY,
        NOT_AUTHORISED,
        USER_DOES_NOT_EXIST,
        USER_NOT_DELETED
    }

    private @Inject UserService service;
    private @Inject IUserRepository userRepository;
    private @Inject IRoleRepository roleRepository;
    private User user;
    private Integer authUserId = null;
    private Integer refUserId = null;

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

    @Override
    public Result execute() throws Exception {

        if(authUserId == null){
            throw new IllegalStateException("authUserId not provided");
        }

        if(refUserId == null){
            throw new IllegalStateException("refUserId not provided");
        }

        if (!service.isUserAnAdmin(new User(authUserId))) {
            return Result.NOT_AUTHORISED;
        }
        else {
            User refUser = new User(refUserId);

            if (!userRepository.findUser(refUser)) {
                return Result.USER_DOES_NOT_EXIST;
            }
            else if (userRepository.deleteUser(refUser)) {
                return Result.USER_DELETED_SUCCESSFULLY;
            }
            else {
                return Result.USER_NOT_DELETED;
            }
        }
    }
}
