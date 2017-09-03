package domain.usecase.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.junit.Test;

import domain.model.RoleModel;
import domain.model.UserModel;

import domain.Helper;
import domain.entity.User;
import domain.usecase.UsecaseTest;

public class UsecaseUpdateExistingUserTest extends UsecaseTest {

    protected UserModel createMockedUserModelObject() throws Exception {

        UserModel model = super.createMockedUserModelObject();

        when(model.updateUser(1, "admin", Helper.SHA1("admin")))
        .thenReturn(true)
        .thenThrow(new Exception("Updating the same user twice"));

        when(model.updateUser(2, "user1", Helper.SHA1("pass1")))
        .thenReturn(true)
        .thenThrow(new Exception("Updating the same user twice"));

        when(model.updateUser(3, "user2", Helper.SHA1("pass2")))
        .thenReturn(true)
        .thenThrow(new Exception("Updating the same user twice"));

        when(model.updateUser(4, "user3", Helper.SHA1("pass3")))
        .thenThrow(new Exception("Attempting to update an inexistant user"));

        when(model.updateUser(2, "admin", Helper.SHA1("admin")))
        .thenThrow(new Exception("Probable constraint violation, attempting to update username to a value already in existance"));

        when(model.updateUser(null, "user2", Helper.SHA1("pass2")))
        .thenThrow(new Exception("Handling invalid update request to user model"));

        when(model.updateUser(3, null, Helper.SHA1("pass2")))
        .thenThrow(new Exception("Handling invalid update request to user model"));

        when(model.updateUser(3, "user2", null))
        .thenThrow(new Exception("Handling invalid update request to user model"));

        return model;
    }

    protected RoleModel createMockedRoleModelObject() throws Exception {

        RoleModel model = super.createMockedRoleModelObject();

        when(model.insertUserHasRoles(3, new Integer[] { 1, 2 }))
        .thenReturn(true)
        .thenThrow(new Exception("Updating the roles twice to the same user"));

        when(model.insertUserHasRoles(null, new Integer[] { 1, 2 }))
        .thenThrow(new Exception("Handling invalid insert request to role model"));

        when(model.insertUserHasRoles(3, null))
        .thenThrow(new Exception("Handling invalid insert request to role model"));

        when(model.insertUserHasRoles(4, new Integer[] { 4 }))
        .thenThrow(new Exception("Inserting the roles to a user that does not exist"));

        when(model.deleteUserHasRoles(3, new Integer[] { 3 })).thenReturn(true)
        .thenThrow(new Exception("Deleting the roles twice to the same user"));

        when(model.deleteUserHasRoles(null, new Integer[] { 3 }))
        .thenThrow(new Exception("Handling invalid delete request to role model"));

        when(model.deleteUserHasRoles(3, null))
        .thenThrow(new Exception("Handling invalid delete request to role model"));

        return model;
    }

    @Test
    public void testUpdateUser_Success() {

        try {
            UserModel userModel = createMockedUserModelObject();
            RoleModel roleModel = createMockedRoleModelObject();

            UsecaseUpdateExistingUser usecase = new UsecaseUpdateExistingUser(userModel, roleModel);
            usecase.setAuthUserId(1);
            usecase.setRefUserId(3);
            usecase.setUserData(new User(0, "user2", "pass2", new Integer[] { 1, 2 }));

            assertEquals(UsecaseUpdateExistingUser.RESULT_USER_UPDATED_SUCCESSFULLY, usecase.execute());
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testUpdateUser_NotAuthorised() {

        try {
            UserModel userModel = createMockedUserModelObject();
            RoleModel roleModel = createMockedRoleModelObject();

            UsecaseUpdateExistingUser usecase = new UsecaseUpdateExistingUser(userModel, roleModel);
            usecase.setAuthUserId(2);
            usecase.setRefUserId(3);
            usecase.setUserData(new User(0, "user2", "pass2", new Integer[] { 3 }));

            assertEquals(UsecaseUpdateExistingUser.RESULT_NOT_AUTHORISED, usecase.execute());
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testUpdateUser_UserDoesNotExist() {

        try {
            UserModel userModel = createMockedUserModelObject();
            RoleModel roleModel = createMockedRoleModelObject();

            UsecaseUpdateExistingUser usecase = new UsecaseUpdateExistingUser(userModel, roleModel);
            usecase.setAuthUserId(1);
            usecase.setRefUserId(4);
            usecase.setUserData(new User(0, "user3", "pass3", new Integer[] { 4 }));

            assertEquals(UsecaseUpdateExistingUser.RESULT_USER_DOES_NOT_EXIST, usecase.execute());
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testUpdateUser_UsernameAlreadyTaken() {

        try {
            UserModel userModel = createMockedUserModelObject();
            RoleModel roleModel = createMockedRoleModelObject();

            UsecaseUpdateExistingUser usecase = new UsecaseUpdateExistingUser(userModel, roleModel);
            usecase.setAuthUserId(1);
            usecase.setRefUserId(2);
            usecase.setUserData(new User(0, "admin", "admin", new Integer[] { 1, 2, 3, 4 }));

            assertEquals(UsecaseUpdateExistingUser.RESULT_USERNAME_ALREADY_TAKEN, usecase.execute());
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testUpdateUser_BadInputData() {

        try {
            UserModel userModel = createMockedUserModelObject();
            RoleModel roleModel = createMockedRoleModelObject();

            UsecaseUpdateExistingUser usecase;

            usecase = new UsecaseUpdateExistingUser(userModel, roleModel);

            try {
                usecase.setAuthUserId(null);
            }
            catch(IllegalArgumentException e){
                assertEquals(e.getMessage(), "authUserId cannot be null");
            }

            try {
                usecase.setRefUserId(null);
            }
            catch(IllegalArgumentException e){
                assertEquals(e.getMessage(), "refUserId cannot be null");
            }

            try {
                usecase.setUserData(null);
            }
            catch(IllegalArgumentException e){
                assertEquals(e.getMessage(), "userData cannot be null");
            }

            try {
                usecase.execute();
            }
            catch(IllegalStateException e){
                assertEquals(e.getMessage(), "authUserId not provided");
            }

            usecase.setAuthUserId(1);

            try {
                usecase.execute();
            }
            catch(IllegalStateException e){
                assertEquals(e.getMessage(), "refUserId not provided");
            }

            usecase.setRefUserId(3);

            try {
                usecase.execute();
            }
            catch(IllegalStateException e){
                assertEquals(e.getMessage(), "userData not provided");
            }

            usecase.setUserData(new User(0, null, "pass2", new Integer[] { 1, 2 }));
            assertEquals(UsecaseUpdateExistingUser.RESULT_BAD_INPUT_DATA, usecase.execute());

            usecase.setUserData(new User(0, "user2", null, new Integer[] { 1, 2 }));
            assertEquals(UsecaseUpdateExistingUser.RESULT_BAD_INPUT_DATA, usecase.execute());

            usecase.setUserData(new User(0, "user2", "pass2", null));
            assertEquals(UsecaseUpdateExistingUser.RESULT_BAD_INPUT_DATA, usecase.execute());
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }
}
