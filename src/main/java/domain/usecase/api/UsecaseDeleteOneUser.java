package domain.usecase.api;

import domain.usecase.Usecase;

import domain.model.RoleModel;
import domain.model.UserModel;

public class UsecaseDeleteOneUser extends Usecase{

    public static final int RESULT_USER_DELETED_SUCCESSFULLY = 1;
    public static final int RESULT_NOT_AUTHORISED = 2;
    public static final int RESULT_USER_DOES_NOT_EXIST = 3;
    public static final int RESULT_USER_NOT_DELETED = 4;

    private UserModel userModel;
    private RoleModel roleModel;

    // Input data
    private Integer authUserId = null;
    private Integer refUserId = null;

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

    public Integer getRefUserId() {
        return refUserId;
    }

    public void setRefUserId(Integer refUserId) {
        if (refUserId == null) {
            throw new IllegalArgumentException("refUserId cannot be null");
        }

        this.refUserId = refUserId;
    }

    // Constructor
    public UsecaseDeleteOneUser(UserModel userModel, RoleModel roleModel) {
        this.userModel = userModel;
        this.roleModel = roleModel;
    }

    // Business Logic
    public int execute() throws Exception {

        if(authUserId == null){
            throw new IllegalStateException("authUserId not provided");
        }

        if(refUserId == null){
            throw new IllegalStateException("refUserId not provided");
        }

        if (userModel.selectUserIsAdminRole(authUserId)) {
            if (userModel.selectUserExists(refUserId)) {
                if (userModel.deleteUser(refUserId)) {
                    if (!roleModel.deleteUserHasRolesByUserId(refUserId)) {
                        return RESULT_USER_NOT_DELETED;
                    }
                }

                return RESULT_USER_DELETED_SUCCESSFULLY;

            } else {
                return RESULT_USER_DOES_NOT_EXIST;
            }
        } else {
            return RESULT_NOT_AUTHORISED;
        }
    }
}
