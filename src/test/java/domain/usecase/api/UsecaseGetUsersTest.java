package domain.usecase.api;

import static org.junit.Assert.*;

import domain.constraints.repository.IRoleRepository;
import domain.constraints.repository.IUserRepository;
import org.junit.Test;

import domain.entity.Role;
import domain.entity.User;
import domain.usecase.UsecaseTest;

public class UsecaseGetUsersTest extends UsecaseTest {

    @Test
    public void testGetUsers() {

        try {

            IUserRepository userRepo = createMockedUserRepositoryObject();
            IRoleRepository roleRepo = createMockedRoleRepositoryObject();

            UsecaseGetUsers usecase = new UsecaseGetUsers(userRepo, roleRepo);

            assertEquals(UsecaseGetUsers.RESULT_USERS_RETRIEVED_SUCCESSFULLY, usecase.execute());

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
