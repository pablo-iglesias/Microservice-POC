package domain.usecase.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import domain.contract.entity.UserObject;
import org.junit.Test;

import domain.entity.User;
import domain.usecase.UsecaseTest;
import org.mockito.InjectMocks;

public class UsecaseUpdateExistingUserTest<Result extends UsecaseUpdateExistingUser.Result> extends UsecaseTest {

    @InjectMocks
    UsecaseUpdateExistingUser usecase;

    protected void initUserRepositoryMock() throws Exception {

        super.initUserRepositoryMock();

        when(userRepository.updateUser(admin))
        .thenReturn(true)
        .thenThrow(new Exception("Updating the same user twice"));

        when(userRepository.updateUser(user2))
        .thenReturn(true)
        .thenThrow(new Exception("Updating the same user twice"));

        when(userRepository.updateUser(new User(3,null, "pass2", new Integer[] { 1, 2 })))
        .thenThrow(new Exception("Handling invalid update request to user repo"));

        when(userRepository.updateUser(new User(3,"user2", "pass2", null)))
        .thenThrow(new Exception("Handling invalid update request to user repo"));
    }

    @Test
    public void testUpdateUser_Success() {

        try {
            usecase.setAuthUserId(1);
            usecase.setRefUserId(3);
            usecase.setUserData(new Gson().fromJson("{username:'user2', password:'pass2', roles:[3]}", UsecaseTest.UserObject.class));

            assertEquals(Result.USER_UPDATED_SUCCESSFULLY, usecase.execute());
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testUpdateUser_NotAuthorised() {

        try {
            usecase.setAuthUserId(2);
            usecase.setRefUserId(3);
            usecase.setUserData(user2);

            assertEquals(Result.NOT_AUTHORISED, usecase.execute());
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testUpdateUser_UserDoesNotExist() {

        try {
            usecase.setAuthUserId(1);
            usecase.setRefUserId(4);
            usecase.setUserData(user3);

            assertEquals(Result.USER_DOES_NOT_EXIST, usecase.execute());
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testUpdateUser_UsernameAlreadyTaken() {

        try {
            usecase.setAuthUserId(1);
            usecase.setRefUserId(2);
            usecase.setUserData(admin);

            assertEquals(Result.USERNAME_ALREADY_TAKEN, usecase.execute());
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testUpdateUser_BadInputData() {

        try {
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

            usecase.setUserData(new User(3, null, "pass2", new Integer[] { 1, 2 }));
            assertEquals(Result.BAD_INPUT_DATA, usecase.execute());

            usecase.setUserData(new User(3, "user2", null, new Integer[] { 1, 2 }));
            assertEquals(Result.BAD_INPUT_DATA, usecase.execute());

            usecase.setUserData(new User(3, "user2", "pass2", null));
            assertEquals(Result.BAD_INPUT_DATA, usecase.execute());
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }
}
