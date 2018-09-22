package domain.usecase.api;

import static org.junit.Assert.*;

import org.junit.Test;

import domain.usecase.UsecaseTest;
import org.mockito.InjectMocks;

public class UsecaseGetUsersTest<Result extends UsecaseGetUsers.Result> extends UsecaseTest {

    @InjectMocks
    UsecaseGetUsers usecase;

    @Test
    public void testGetUsers() {

        try {
            assertEquals(Result.USERS_RETRIEVED_SUCCESSFULLY, usecase.execute());

            assertEquals(admin, usecase.getUsers()[0]);
            assertEquals(user1, usecase.getUsers()[1]);
            assertEquals(user2, usecase.getUsers()[2]);

            assertEquals(role1, usecase.getRoles()[0]);
            assertEquals(role2, usecase.getRoles()[1]);
            assertEquals(role3, usecase.getRoles()[2]);
            assertEquals(role4, usecase.getRoles()[3]);
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }
}
