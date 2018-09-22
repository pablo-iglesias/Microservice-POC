package domain.usecase;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.InjectMocks;

public class UsecaseAuthenticateUserTest<Result extends UsecaseAuthenticateUser.Result> extends UsecaseTest {

    @InjectMocks
    UsecaseAuthenticateUser usecase;

    @Test
    public void testAuthenticate_GoodCredentials() {

        try {
            usecase.setUsername("user1");
            usecase.setPassword("pass1");

            assertEquals(Result.USER_AUTHENTICATED_SUCCESSFULLY, usecase.execute());
            assertEquals(true, usecase.getRefUserId() == 2);

            usecase.setUsername("user2");
            usecase.setPassword("pass2");

            assertEquals(Result.USER_AUTHENTICATED_SUCCESSFULLY, usecase.execute());
            assertEquals(true, usecase.getRefUserId() == 3);
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testAuthenticate_BadCredentials() {

        try {
            usecase.setUsername("user1");
            usecase.setPassword("pass2");

            assertEquals(Result.DID_NOT_AUTHENTICATE, usecase.execute());
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }
}
