package domain.usecase.api;

import domain.usecase.Usecase;

import domain.Helper;
import domain.entity.User;

import domain.model.UserModel;
import domain.model.RoleModel;

public class UsecaseAddNewUser extends Usecase{

    public static final int RESULT_USER_CREATED_SUCCESSFULLY = 1;
    public static final int RESULT_NOT_AUTHORISED = 2;
    public static final int RESULT_USER_ALREADY_EXISTS = 3;
    public static final int RESULT_USER_NOT_CREATED = 4;
    public static final int RESULT_BAD_INPUT_DATA = 5;

    private UserModel userModel;
    private RoleModel roleModel;

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

    public User getUserData() {
        return userData;
    }

    public void setUserData(User userData) {
        if (userData == null) {
            throw new IllegalArgumentException("userData cannot be null");
        }

        this.userData = userData;
    }

    // Constructor
    public UsecaseAddNewUser(UserModel userModel, RoleModel roleModel) {
        this.userModel = userModel;
        this.roleModel = roleModel;
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
            boolean allowed = userModel.selectUserIsAdminRole(authUserId.intValue());
            
            if (allowed) {
                if (userModel.selectUserExistsByUseraname(userData.getUsername())) {
                    return RESULT_USER_ALREADY_EXISTS;
                }

                Integer newUserId = userModel.insertUser(
                        userData.getUsername(),
                        Helper.SHA1(userData.getPassword())
                );

                if (newUserId != null) {
                    if (roleModel.insertUserHasRoles(newUserId, userData.getRoles())) {
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
