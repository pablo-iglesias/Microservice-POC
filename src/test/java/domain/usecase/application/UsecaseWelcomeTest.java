package domain.usecase.application;

import static org.junit.Assert.*;

import org.junit.Test;

import domain.usecase.UsecaseTest;
import org.mockito.InjectMocks;

public class UsecaseWelcomeTest<Result extends UsecaseWelcome.Result> extends UsecaseTest {

    @InjectMocks
    UsecaseWelcome usecase;

    @Test
    public void testWelcome_Success() {

        try {
            usecase.setRefUserId(1);

            assertEquals(Result.USER_RETRIEVED_SUCCESSFULLY, usecase.execute());
            assertEquals("admin", usecase.getUsername());

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

    @Test
    public void testWelcome_Inexistant() {

        try {
            usecase.setRefUserId(4);

            assertEquals(Result.USER_NOT_FOUND, usecase.execute());
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }
}
