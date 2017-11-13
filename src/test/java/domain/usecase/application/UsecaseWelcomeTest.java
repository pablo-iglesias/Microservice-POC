package domain.usecase.application;

import static org.junit.Assert.*;

import org.junit.Test;

import domain.entity.Role;
import domain.entity.factory.RoleFactory;
import domain.entity.factory.UserFactory;
import domain.usecase.UsecaseTest;

public class UsecaseWelcomeTest extends UsecaseTest {

    @Test
    public void testWelcome_Success() {

        try {

            UserFactory userFactory = createMockedUserFactoryObject();
            RoleFactory roleFactory = createMockedRoleFactoryObject();

            UsecaseWelcome usecase = new UsecaseWelcome(userFactory, roleFactory);
            usecase.setRefUserId(1);

            assertEquals(UsecaseWelcome.RESULT_USER_RETRIEVED_SUCCESSFULLY, usecase.execute());
            assertEquals("admin", usecase.getUsername());

            assertEquals(new Role(1, "ADMIN", ""), usecase.getRoles()[0]);
            assertEquals(new Role(2, "PAGE_1", "page_1"), usecase.getRoles()[1]);
            assertEquals(new Role(3, "PAGE_2", "page_2"), usecase.getRoles()[2]);
            assertEquals(new Role(4, "PAGE_3", "page_3"), usecase.getRoles()[3]);
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testWelcome_Inexistant() {

        try {

            UserFactory userFactory = createMockedUserFactoryObject();
            RoleFactory roleFactory = createMockedRoleFactoryObject();

            UsecaseWelcome usecase = new UsecaseWelcome(userFactory, roleFactory);
            usecase.setRefUserId(4);

            assertEquals(UsecaseWelcome.RESULT_USER_NOT_FOUND, usecase.execute());
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }
}
