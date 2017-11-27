package domain.usecase.api;

import static org.junit.Assert.*;

import domain.constraints.repository.IRoleRepository;
import domain.constraints.repository.IUserRepository;
import domain.entity.User;
import org.junit.Test;

import domain.entity.Role;

import domain.usecase.UsecaseTest;

public class UsecaseGetOneUserTest extends UsecaseTest {

    @Test
    public void testGetOneUser_Success() {

        try {

            IUserRepository userRepository = createMockedUserRepositoryObject();
            IRoleRepository roleRepository = createMockedRoleRepositoryObject();

            UsecaseGetOneUser usecase = new UsecaseGetOneUser(userRepository, roleRepository);
            usecase.setRefUserId(1);

            assertEquals(UsecaseGetOneUser.RESULT_USER_RETRIEVED_SUCCESSFULLY, usecase.execute());

            assertEquals(admin, usecase.getUser());

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
    public void testGetOneUser_Inexistant() {

        try {

            IUserRepository userRepository = createMockedUserRepositoryObject();
            IRoleRepository roleRepository = createMockedRoleRepositoryObject();

            UsecaseGetOneUser usecase = new UsecaseGetOneUser(userRepository, roleRepository);
            usecase.setRefUserId(4);

            assertEquals(UsecaseGetOneUser.RESULT_USER_NOT_FOUND, usecase.execute());
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }
}
