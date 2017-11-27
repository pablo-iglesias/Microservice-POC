package domain.usecase.application;

import static org.junit.Assert.*;

import domain.constraints.repository.IRoleRepository;
import domain.constraints.repository.IUserRepository;
import org.junit.Test;

import domain.entity.Role;
import domain.usecase.UsecaseTest;

public class UsecaseWelcomeTest extends UsecaseTest {

    @Test
    public void testWelcome_Success() {

        try {
            IUserRepository userRepo = createMockedUserRepositoryObject();
            IRoleRepository roleRepo = createMockedRoleRepositoryObject();

            UsecaseWelcome usecase = new UsecaseWelcome(userRepo, roleRepo);
            usecase.setRefUserId(1);

            assertEquals(UsecaseWelcome.RESULT_USER_RETRIEVED_SUCCESSFULLY, usecase.execute());
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
            IUserRepository userRepo = createMockedUserRepositoryObject();
            IRoleRepository roleRepo = createMockedRoleRepositoryObject();

            UsecaseWelcome usecase = new UsecaseWelcome(userRepo, roleRepo);
            usecase.setRefUserId(4);

            assertEquals(UsecaseWelcome.RESULT_USER_NOT_FOUND, usecase.execute());
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }
}
