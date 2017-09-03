package domain.usecase.api;

import static org.junit.Assert.*;

import org.junit.Test;

import domain.entity.Role;
import domain.entity.User;
import domain.factory.RoleFactory;
import domain.factory.UserFactory;
import domain.usecase.UsecaseTest;

public class UsecaseGetUsersTest extends UsecaseTest {

    @Test
    public void testGetUsers() {

        try {

            UserFactory userFactory = createMockedUserFactoryObject();
            RoleFactory roleFactory = createMockedRoleFactoryObject();

            UsecaseGetUsers usecase = new UsecaseGetUsers(userFactory, roleFactory);

            assertEquals(UsecaseGetUsers.RESULT_USERS_RETRIEVED_SUCCESSFULLY, usecase.execute());

            assertEquals(new User(1, "admin", new Integer[] { 1, 2, 3, 4 }), usecase.getUsers()[0]);
            assertEquals(new User(2, "user1", new Integer[] { 2 }), usecase.getUsers()[1]);
            assertEquals(new User(3, "user2", new Integer[] { 3 }), usecase.getUsers()[2]);

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
}
