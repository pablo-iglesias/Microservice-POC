package domain.usecase.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.junit.Test;

import domain.usecase.UsecaseTest;
import org.mockito.InjectMocks;

public class UsecaseDeleteOneUserTest<Result extends UsecaseDeleteOneUser.Result> extends UsecaseTest {

    @InjectMocks
    UsecaseDeleteOneUser usecase;

    protected void initUserRepositoryMock() throws Exception {

        super.initUserRepositoryMock();

        when(userRepository.deleteUser(user2))
        .thenReturn(true)
        .thenThrow(new Exception("Deleting the same user twice"));

        when(userRepository.deleteUser(user3))
        .thenThrow(new Exception("Attempting to delete nonexistent user"));

        when(userRepository.deleteUser(null))
        .thenThrow(new Exception("Handling invalid delete request to user repo"));
    }

    @Test
    public void testDeleteUser_Success() {

        try {
            usecase.setAuthUserId(1);
            usecase.setRefUserId(3);

            assertEquals(Result.USER_DELETED_SUCCESSFULLY, usecase.execute());
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testDeleteUser_NotAuthorised() {

        try {
            usecase.setAuthUserId(2);
            usecase.setRefUserId(3);

            assertEquals(Result.NOT_AUTHORISED, usecase.execute());
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testDeleteUser_UserDoesNotExist() {

        try {
            usecase.setAuthUserId(1);
            usecase.setRefUserId(4);

            assertEquals(Result.USER_DOES_NOT_EXIST, usecase.execute());
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testDeleteUser_BadInputData() {

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
