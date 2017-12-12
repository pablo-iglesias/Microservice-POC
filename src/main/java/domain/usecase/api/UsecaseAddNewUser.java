package domain.usecase.api;

import domain.constraints.UserObject;

import domain.constraints.repository.IRoleRepository;
import domain.constraints.repository.IUserRepository;

import domain.entity.User;
import domain.service.UserService;
import domain.usecase.Usecase;

public class UsecaseAddNewUser extends Usecase{

    public static final int RESULT_USER_CREATED_SUCCESSFULLY = 1;
    public static final int RESULT_NOT_AUTHORISED = 2;
    public static final int RESULT_USER_ALREADY_EXISTS = 3;
    public static final int RESULT_USER_NOT_CREATED = 4;
    public static final int RESULT_BAD_INPUT_DATA = 5;

    private UserService service;
    private IUserRepository userRepository;

    // Input data
    private User authUser = null;  // Authorised user id
    private User newUser = null;        // New user

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

    // Constructor
    public UsecaseAddNewUser(IUserRepository userRepository, IRoleRepository roleRepository) {
        service = new UserService(userRepository, roleRepository);
        this.userRepository = userRepository;
    }

    // Business Logic
    public int execute() throws Exception {

        if (authUser == null) {
            throw new IllegalStateException("authUserId not provided");
        }
        else if (newUser == null) {
            throw new IllegalStateException("userData not provided");
        }
        else if (!newUser.containsValidData()) {
            return RESULT_BAD_INPUT_DATA;
        }
        else if (!service.isUserAnAdmin(authUser)) {
            return RESULT_NOT_AUTHORISED;
        }
        else if (userRepository.findUser(newUser)) {
            return RESULT_USER_ALREADY_EXISTS;
        }
        else if (service.createNewUser(newUser)) {
            return RESULT_USER_CREATED_SUCCESSFULLY;
        }
        else {
            return RESULT_USER_NOT_CREATED;
        }
    }
}
