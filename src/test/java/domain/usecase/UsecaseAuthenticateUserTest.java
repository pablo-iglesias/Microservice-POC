package domain.usecase;

import static org.junit.Assert.*;

import org.junit.Test;

import domain.factory.UserFactory;

public class UsecaseAuthenticateUserTest extends UsecaseTest {

    @Test
    public void testAuthenticate_GoodCredentials() {

        try {

            UserFactory factory = createMockedUserFactoryObject();

            UsecaseAuthenticateUser usecase;

            usecase = new UsecaseAuthenticateUser(factory);
            usecase.setUsername("user1");
            usecase.setPassword("pass1");

            assertEquals(UsecaseAuthenticateUser.RESULT_USER_AUTHENTICATED_SUCCESSFULLY, usecase.execute());
            assertEquals(true, usecase.getRefUserId() == 2);

            usecase = new UsecaseAuthenticateUser(factory);
            usecase.setUsername("user2");
            usecase.setPassword("pass2");

            assertEquals(UsecaseAuthenticateUser.RESULT_USER_AUTHENTICATED_SUCCESSFULLY, usecase.execute());
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
            UserFactory factory = createMockedUserFactoryObject();

            UsecaseAuthenticateUser usecase;

            usecase = new UsecaseAuthenticateUser(factory);
            usecase.setUsername("user1");
            usecase.setPassword("pass2");

            assertEquals(UsecaseAuthenticateUser.RESULT_DID_NOT_AUTHENTICATE, usecase.execute());
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }
}
