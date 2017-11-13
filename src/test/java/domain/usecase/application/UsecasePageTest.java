package domain.usecase.application;

import static org.junit.Assert.*;

import org.junit.Test;

import domain.entity.factory.RoleFactory;
import domain.entity.factory.UserFactory;
import domain.usecase.UsecaseTest;

public class UsecasePageTest extends UsecaseTest {

    @Test
    public void pageTest_Success() {

        try {

            UserFactory userFactory = createMockedUserFactoryObject();
            RoleFactory roleFactory = createMockedRoleFactoryObject();

            UsecasePage usecase = new UsecasePage(userFactory, roleFactory);
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
    public void pageTest_Inexistant() {

        try {

            UserFactory userFactory = createMockedUserFactoryObject();
            RoleFactory roleFactory = createMockedRoleFactoryObject();

            UsecasePage usecase = new UsecasePage(userFactory, roleFactory);
            usecase.setRefUserId(4);
            usecase.setPage(1);

            assertEquals(UsecasePage.RESULT_PAGE_NOT_FOUND, usecase.execute());
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void pageTest_NotAuthorised() {

        try {

            UserFactory userFactory = createMockedUserFactoryObject();
            RoleFactory roleFactory = createMockedRoleFactoryObject();

            UsecasePage usecase = new UsecasePage(userFactory, roleFactory);
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
