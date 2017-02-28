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
            usecase.username = "user1";
            usecase.password = "pass1";

            assertEquals(true, usecase.execute() && usecase.uid == 2);

            usecase = new UsecaseAuthenticateUser(factory);
            usecase.username = "user2";
            usecase.password = "pass2";

            assertEquals(true, usecase.execute() && usecase.uid == 3);
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
            usecase.username = "user1";
            usecase.password = "pass2";

            assertEquals(false, usecase.execute());
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }
}
