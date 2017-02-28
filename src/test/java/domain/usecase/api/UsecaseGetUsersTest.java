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

            assertEquals(true, usecase.execute());

            assertEquals(new User(1, "admin", new Integer[] { 1, 2, 3, 4 }), usecase.users[0]);
            assertEquals(new User(2, "user1", new Integer[] { 2 }), usecase.users[1]);
            assertEquals(new User(3, "user2", new Integer[] { 3 }), usecase.users[2]);

            assertEquals(new Role(1, "ADMIN", ""), usecase.roles[0]);
            assertEquals(new Role(2, "PAGE_1", "page_1"), usecase.roles[1]);
            assertEquals(new Role(3, "PAGE_2", "page_2"), usecase.roles[2]);
            assertEquals(new Role(4, "PAGE_3", "page_3"), usecase.roles[3]);
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }
}
