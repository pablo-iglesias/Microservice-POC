package domain.usecase.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.junit.Test;

import adapter.repository.RoleRepository;
import adapter.repository.UserRepository;

import domain.usecase.UsecaseTest;

public class UsecaseDeleteOneUserTest extends UsecaseTest {

    protected UserRepository createMockedUserModelObject() throws Exception {

        UserRepository model = super.createMockedUserModelObject();

        when(model.deleteUser(3))
        .thenReturn(true)
        .thenThrow(new Exception("Deleting the same user twice"));

        when(model.deleteUser(4))
        .thenThrow(new Exception("Attempting to delete unexistant user"));

        when(model.deleteUser(null))
        .thenThrow(new Exception("Handling invalid delete request to user model"));

        return model;
    }

    protected RoleRepository createMockedRoleModelObject() throws Exception {

        RoleRepository model = super.createMockedRoleModelObject();

        when(model.deleteUserHasRolesByUserId(3))
        .thenReturn(true)
        .thenThrow(new Exception("Deleting roles to the same user twice"));

        when(model.deleteUserHasRolesByUserId(4))
        .thenThrow(new Exception("Attempting to delete roles to unexistant user"));

        when(model.deleteUserHasRolesByUserId(null))
        .thenThrow(new Exception("Handling invalid delete request to role model"));

        return model;
    }

    @Test
    public void testDeleteUser_Success() {

        try {
            UserRepository userModel = createMockedUserModelObject();
            RoleRepository roleModel = createMockedRoleModelObject();

            UsecaseDeleteOneUser usecase = new UsecaseDeleteOneUser(userModel, roleModel);
            usecase.setAuthUserId(1);
            usecase.setRefUserId(3);

            assertEquals(UsecaseDeleteOneUser.RESULT_USER_DELETED_SUCCESSFULLY, usecase.execute());
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testDeleteUser_NotAuthorised() {

        try {
            UserRepository userModel = createMockedUserModelObject();
            RoleRepository roleModel = createMockedRoleModelObject();

            UsecaseDeleteOneUser usecase = new UsecaseDeleteOneUser(userModel, roleModel);
            usecase.setAuthUserId(2);
            usecase.setRefUserId(3);

            assertEquals(UsecaseDeleteOneUser.RESULT_NOT_AUTHORISED, usecase.execute());
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testDeleteUser_UserDoesNotExist() {

        try {
            UserRepository userModel = createMockedUserModelObject();
            RoleRepository roleModel = createMockedRoleModelObject();

            UsecaseDeleteOneUser usecase = new UsecaseDeleteOneUser(userModel, roleModel);
            usecase.setAuthUserId(1);
            usecase.setRefUserId(4);

            assertEquals(UsecaseDeleteOneUser.RESULT_USER_DOES_NOT_EXIST, usecase.execute());
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testDeleteUser_BadInputData() {

        try {
            UserRepository userModel = createMockedUserModelObject();
            RoleRepository roleModel = createMockedRoleModelObject();

            UsecaseDeleteOneUser usecase;

            usecase = new UsecaseDeleteOneUser(userModel, roleModel);

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
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }
}
