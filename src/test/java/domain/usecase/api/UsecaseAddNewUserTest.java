package domain.usecase.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.junit.Test;

import adapter.repository.RoleRepository;
import adapter.repository.UserRepository;

import domain.Helper;
import domain.entity.User;
import domain.usecase.UsecaseTest;

public class UsecaseAddNewUserTest extends UsecaseTest {

    protected UserRepository createMockedUserModelObject() throws Exception {

        UserRepository model = super.createMockedUserModelObject();

        when(model.insertUser("user3", Helper.SHA1("pass3")))
        .thenReturn(4)
        .thenThrow(new Exception("Inserting the same user twice"));

        when(model.insertUser("admin", Helper.SHA1("admin")))
        .thenThrow(new Exception("Probable constraint violation, attempting to insert username with a value already in existance"));

        when(model.insertUser("user1", Helper.SHA1("pass1")))
        .thenThrow(new Exception("Probable constraint violation, attempting to insert username with a value already in existance"));

        when(model.insertUser("user2", Helper.SHA1("pass2")))
        .thenThrow(new Exception("Probable constraint violation, attempting to insert username with a value already in existance"));

        return model;
    }

    protected RoleRepository createMockedRoleModelObject() throws Exception {

        RoleRepository model = super.createMockedRoleModelObject();

        when(model.insertUserHasRoles(4, new Integer[] { 4 }))
        .thenReturn(true)
        .thenThrow(new Exception("Adding the same roles twice to the user"));

        when(model.insertUserHasRoles(1, new Integer[] { 4 }))
        .thenThrow(new Exception("Adding the roles to a preexisting user"));

        when(model.insertUserHasRoles(2, new Integer[] { 4 }))
        .thenThrow(new Exception("Adding the roles to a preexisting user"));

        when(model.insertUserHasRoles(3, new Integer[] { 4 }))
        .thenThrow(new Exception("Adding the roles to a preexisting user"));

        return model;
    }

    @Test
    public void testAddNewUser_Success() {

        try {
            UserRepository userModel = createMockedUserModelObject();
            RoleRepository roleModel = createMockedRoleModelObject();

            UsecaseAddNewUser usecase = new UsecaseAddNewUser(userModel, roleModel);
            usecase.setAuthUserId(1);
            usecase.setUserData(new User(0, "user3", "pass3", new Integer[] { 4 }));

            assertEquals(UsecaseAddNewUser.RESULT_USER_CREATED_SUCCESSFULLY, usecase.execute());
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testAddNewUser_NotAuthorised() {

        try {
            UserRepository userModel = createMockedUserModelObject();
            RoleRepository roleModel = createMockedRoleModelObject();

            UsecaseAddNewUser usecase = new UsecaseAddNewUser(userModel, roleModel);
            usecase.setAuthUserId(2);
            usecase.setUserData(new User(0, "user3", "pass3", new Integer[] { 4 }));

            assertEquals(UsecaseAddNewUser.RESULT_NOT_AUTHORISED, usecase.execute());
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testAddNewUser_UserAlreadyExists() {

        try {
            UserRepository userModel = createMockedUserModelObject();
            RoleRepository roleModel = createMockedRoleModelObject();

            UsecaseAddNewUser usecase = new UsecaseAddNewUser(userModel, roleModel);
            usecase.setAuthUserId(1);
            usecase.setUserData(new User(0, "admin", "admin", new Integer[] { 4 }));

            assertEquals(UsecaseAddNewUser.RESULT_USER_ALREADY_EXISTS, usecase.execute());
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testAddNewUser_BadInputData() {

        try {
            UserRepository userModel = createMockedUserModelObject();
            RoleRepository roleModel = createMockedRoleModelObject();

            UsecaseAddNewUser usecase = new UsecaseAddNewUser(userModel, roleModel);

            try {
                usecase.setAuthUserId(null);
            }
            catch(IllegalArgumentException e){
                assertEquals(e.getMessage(), "authUserId cannot be null");
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
                assertEquals(e.getMessage(), "userData not provided");
            }

            usecase.setUserData(new User(0, "", "", new Integer[0]));

            assertEquals(UsecaseAddNewUser.RESULT_BAD_INPUT_DATA, usecase.execute());
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }
}
