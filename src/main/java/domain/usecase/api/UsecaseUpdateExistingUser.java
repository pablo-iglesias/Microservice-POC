package domain.usecase.api;

import domain.constraints.UserObject;
import domain.constraints.repository.IRoleRepository;
import domain.constraints.repository.IUserRepository;

import domain.entity.User;
import domain.entity.Role;

import domain.usecase.Usecase;
import domain.service.UserService;

public class UsecaseUpdateExistingUser extends Usecase{

    public static final int RESULT_USER_UPDATED_SUCCESSFULLY = 1;
    public static final int RESULT_NOT_AUTHORISED = 2;
    public static final int RESULT_USER_DOES_NOT_EXIST = 3;
    public static final int RESULT_USERNAME_ALREADY_TAKEN = 4;
    public static final int RESULT_USER_NOT_UPDATED = 5;
    public static final int RESULT_BAD_INPUT_DATA = 6;

    private UserService service;
    private IUserRepository userRepository;
    private IRoleRepository roleRepository;

    private User user = null;

    // Input data
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

    // Constructor
    public UsecaseUpdateExistingUser(IUserRepository userRepository, IRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        service = new UserService(userRepository, roleRepository);
    }

    // Business Logic
    public int execute() throws Exception {

        if (authUserId == null){
            throw new IllegalStateException("authUserId not provided");
        }
        else if (refUserId == null){
            throw new IllegalStateException("refUserId not provided");
        }
        else if (userData == null){
            throw new IllegalStateException("userData not provided");
        }
        else if (userData.getUsername() == null || userData.getUsername().length() == 0 ||
                userData.getPassword() == null || userData.getPassword().length() == 0 ||
                userData.getRoleIds() == null || userData.getRoleIds().length == 0) {
            return RESULT_BAD_INPUT_DATA;
        }
        else if (!service.isUserAnAdmin(authUserId)) {
            return RESULT_NOT_AUTHORISED;
        }
        else if (!service.doesUserExist(refUserId)) {
            return RESULT_USER_DOES_NOT_EXIST;
        }
        else {
            user = new User(userData);
            user.setId(refUserId);

            User usernameHolder = userRepository.getUser(user.getUsername());
            if (usernameHolder != null && !user.equals(usernameHolder)) {
                return RESULT_USERNAME_ALREADY_TAKEN;
            }
            else if (!service.updateUser(user)) {
                return RESULT_USER_NOT_UPDATED;
            }

            return RESULT_USER_UPDATED_SUCCESSFULLY;
        }
    }
}
