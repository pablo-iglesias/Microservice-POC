package domain.usecase.api;

import domain.model.UserModel;
import domain.usecase.Usecase;

import domain.Helper;
import domain.entity.User;

import adapter.repository.UserRepository;
import adapter.repository.RoleRepository;

public class UsecaseAddNewUser extends Usecase{

    public static final int RESULT_USER_CREATED_SUCCESSFULLY = 1;
    public static final int RESULT_NOT_AUTHORISED = 2;
    public static final int RESULT_USER_ALREADY_EXISTS = 3;
    public static final int RESULT_USER_NOT_CREATED = 4;
    public static final int RESULT_BAD_INPUT_DATA = 5;

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    // Input data
    private Integer authUserId = null;
    private User userData = null;

    // Getter & Setter
    public Integer getAuthUserId() {
        return authUserId;
    }

    public void setAuthUserId(Integer authUserId) {
        if (authUserId == null) {
            throw new IllegalArgumentException("authUserId cannot be null");
        }

        this.authUserId = authUserId;
    }

    public UserModel getUserData() {
        return userData;
    }

    public void setUserData(UserModel userData) {
        if (userData == null) {
            throw new IllegalArgumentException("userData cannot be null");
        }

        this.userData = new User(userData);
    }

    // Constructor
    public UsecaseAddNewUser(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    // Business Logic
    public int execute() throws Exception {

        if (authUserId == null) {
            throw new IllegalStateException("authUserId not provided");
        }

        if (userData == null) {
            throw new IllegalStateException("userData not provided");
        }

        if ( userData.getUsername() != null && userData.getUsername().length() > 0 &&
             userData.getPassword() != null && userData.getPassword().length() > 0 &&
             userData.getRoles() != null && userData.getRoles().length > 0)
        {
            boolean allowed = userRepository.selectUserIsAdminRole(authUserId.intValue());
            
            if (allowed) {
                if (userRepository.selectUserExistsByUseraname(userData.getUsername())) {
                    return RESULT_USER_ALREADY_EXISTS;
                }

                Integer newUserId = userRepository.insertUser(
                        userData.getUsername(),
                        Helper.SHA1(userData.getPassword())
                );

                if (newUserId != null) {
                    if (roleRepository.insertUserHasRoles(newUserId, userData.getRoles())) {
                        return RESULT_USER_CREATED_SUCCESSFULLY;
                    }
                }

                return RESULT_USER_NOT_CREATED;
            } 
            else {
                return RESULT_NOT_AUTHORISED;
            }
        }

        return RESULT_BAD_INPUT_DATA;
    }
}
