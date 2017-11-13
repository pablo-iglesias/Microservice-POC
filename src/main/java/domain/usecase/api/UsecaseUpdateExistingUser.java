package domain.usecase.api;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import domain.model.UserModel;
import domain.usecase.Usecase;

import domain.Helper;
import domain.entity.User;

import adapter.repository.RoleRepository;
import adapter.repository.UserRepository;

public class UsecaseUpdateExistingUser extends Usecase{

    public static final int RESULT_USER_UPDATED_SUCCESSFULLY = 1;
    public static final int RESULT_NOT_AUTHORISED = 2;
    public static final int RESULT_USER_DOES_NOT_EXIST = 3;
    public static final int RESULT_USERNAME_ALREADY_TAKEN = 4;
    public static final int RESULT_USER_NOT_UPDATED = 5;
    public static final int RESULT_BAD_INPUT_DATA = 6;

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    // Input data
    private Integer authUserId = null;
    private Integer refUserId = null;
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

    public Integer getRefUserId() {
        return refUserId;
    }

    public void setRefUserId(Integer refUserId) {
        if (refUserId == null) {
            throw new IllegalArgumentException("refUserId cannot be null");
        }

        this.refUserId = refUserId;
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
    public UsecaseUpdateExistingUser(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    // Business Logic
    public int execute() throws Exception {

        if(authUserId == null){
            throw new IllegalStateException("authUserId not provided");
        }

        if(refUserId == null){
            throw new IllegalStateException("refUserId not provided");
        }

        if(userData == null){
            throw new IllegalStateException("userData not provided");
        }

        if ( userData.getUsername() != null && userData.getUsername().length() > 0 &&
             userData.getPassword() != null && userData.getPassword().length() > 0 &&
             userData.getRoles() != null && userData.getRoles().length > 0)
        {
            if (userRepository.selectUserIsAdminRole(authUserId))
            {
                if (userRepository.selectUserExists(refUserId))
                {
                    Integer currentUsernameHolder = userRepository.selectUserIdByUseraname(userData.getUsername());
                    if (currentUsernameHolder == null || currentUsernameHolder.equals(refUserId)) 
                    {
                        String hash = Helper.SHA1(userData.getPassword());
                        if (!userRepository.updateUser(refUserId, userData.getUsername(), hash)) {
                            return RESULT_USER_NOT_UPDATED;
                        }

                        Integer[] currentRoles = roleRepository.getRoleIdsByUserId(refUserId);
                        Integer[] requiredRoles = userData.getRoles();

                        List<Integer> currentRolesList = Arrays.asList(currentRoles);
                        List<Integer> requiredRolesList = Arrays.asList(requiredRoles);

                        Vector<Integer> addRoles = new Vector<Integer>();
                        Vector<Integer> deleteRoles = new Vector<Integer>();

                        for (int required : requiredRoles) {
                            if (!currentRolesList.contains(required)) {
                                addRoles.add(required);
                            }
                        }

                        for (int current : currentRoles) {
                            if (!requiredRolesList.contains(current)) {
                                deleteRoles.add(current);
                            }
                        }

                        if (addRoles.size() != 0) {
                            if (!roleRepository.insertUserHasRoles(refUserId, addRoles.toArray(new Integer[0]))) {
                                return RESULT_USER_NOT_UPDATED;
                            }
                        }

                        if (deleteRoles.size() != 0) {
                            if (!roleRepository.deleteUserHasRoles(refUserId, deleteRoles.toArray(new Integer[0]))) {
                                return RESULT_USER_NOT_UPDATED;
                            }
                        }

                        return RESULT_USER_UPDATED_SUCCESSFULLY;
                    } 
                    else {
                        return RESULT_USERNAME_ALREADY_TAKEN;
                    }
                } else {
                    return RESULT_USER_DOES_NOT_EXIST;
                }
            } else {
                return RESULT_NOT_AUTHORISED;
            }
        } else {
            return RESULT_BAD_INPUT_DATA;
        }
    }
}
