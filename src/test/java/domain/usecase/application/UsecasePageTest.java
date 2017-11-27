package domain.usecase.application;

import static org.junit.Assert.*;

import domain.constraints.repository.IRoleRepository;
import domain.constraints.repository.IUserRepository;
import org.junit.Test;

import domain.usecase.UsecaseTest;

public class UsecasePageTest extends UsecaseTest {

    @Test
    public void pageTest_Success() {

        try {

            IUserRepository userRepo = createMockedUserRepositoryObject();
            IRoleRepository roleRepo = createMockedRoleRepositoryObject();

            UsecasePage usecase = new UsecasePage(userRepo, roleRepo);
            usecase.setRefUserId(2);
            usecase.setPage(1);

            assertEquals(UsecasePage.RESULT_PAGE_RETRIEVED_SUCCESSFULLY, usecase.execute());
            assertEquals("user1", usecase.getUsername());
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void pageTest_NotAuthorised() {

        try {

            IUserRepository userRepo = createMockedUserRepositoryObject();
            IRoleRepository roleRepo = createMockedRoleRepositoryObject();

            UsecasePage usecase = new UsecasePage(userRepo, roleRepo);
            usecase.setRefUserId(3);
            usecase.setPage(1);

            assertEquals(UsecasePage.RESULT_PAGE_NOT_ALLOWED, usecase.execute());
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }
}
