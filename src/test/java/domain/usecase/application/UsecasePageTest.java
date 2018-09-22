package domain.usecase.application;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.junit.Test;

import domain.usecase.UsecaseTest;
import org.mockito.InjectMocks;

public class UsecasePageTest<Result extends UsecasePage.Result> extends UsecaseTest {

    @InjectMocks
    UsecasePage usecase;

    protected void initUserServiceMock() throws Exception {

        super.initUserServiceMock();

        when(service.isUserAllowedIntoPage(2, 1)).thenReturn(true);
        when(service.isUserAllowedIntoPage(3, 1)).thenReturn(false);
    }

    @Test
    public void pageTest_Success() {

        try {
            usecase.setRefUserId(2);
            usecase.setPage(1);

            assertEquals(Result.PAGE_RETRIEVED_SUCCESSFULLY, usecase.execute());
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
            usecase.setRefUserId(3);
            usecase.setPage(1);

            assertEquals(Result.PAGE_NOT_ALLOWED, usecase.execute());
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }
}
