package domain.usecase.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.junit.Test;

import domain.entity.User;
import domain.usecase.UsecaseTest;

import com.google.gson.Gson;
import org.mockito.InjectMocks;

public class UsecaseAddNewUserTest<Result extends UsecaseAddNewUser.Result> extends UsecaseTest {

    @InjectMocks
    UsecaseAddNewUser usecase;

    protected void initUserServiceMock() throws Exception {

        super.initUserServiceMock();

        Gson gson = new Gson();
        UserObject userData = gson.fromJson("{username: 'user3', password: 'pass3', roles: [4]}", UsecaseTest.UserObject.class);

        when(service.createNewUser(new User(userData)))
                .thenReturn(true)
                .thenThrow(new Exception("Creating the same user twice"));
    }

    @Test
    public void testAddNewUser_Success() {

        try {
            usecase.setAuthUserId(1);

            Gson gson = new Gson();
            UserObject user = gson.fromJson("{username: 'user3', password: 'pass3', roles: [4]}", UsecaseTest.UserObject.class);

            usecase.setUserData(user);

            assertEquals(Result.USER_CREATED_SUCCESSFULLY, usecase.execute());
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testAddNewUser_NotAuthorised() {

        try {
            usecase.setAuthUserId(2);

            Gson gson = new Gson();
            UserObject user = gson.fromJson("{username: 'user3', password: 'pass3', roles: [4]}", UsecaseTest.UserObject.class);

            usecase.setUserData(user);

            assertEquals(Result.NOT_AUTHORISED, usecase.execute());
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testAddNewUser_UserAlreadyExists() {

        try {
            usecase.setAuthUserId(1);

            Gson gson = new Gson();
            UserObject user = gson.fromJson("{username: 'admin', password: 'admin', roles: [1,2,3,4]}", UsecaseTest.UserObject.class);

            usecase.setUserData(user);

            assertEquals(Result.USER_ALREADY_EXISTS, usecase.execute());
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testAddNewUser_BadInputData() {

        try {
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

            assertEquals(Result.BAD_INPUT_DATA, usecase.execute());
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }
}
