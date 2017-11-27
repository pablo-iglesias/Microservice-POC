package domain.usecase.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.junit.Test;

import domain.constraints.repository.IRoleRepository;
import domain.constraints.repository.IUserRepository;

import domain.usecase.UsecaseTest;

public class UsecaseDeleteOneUserTest extends UsecaseTest {

    protected IUserRepository createMockedUserRepositoryObject() throws Exception {

        IUserRepository userRepo = super.createMockedUserRepositoryObject();

        when(userRepo.deleteUser(3))
        .thenReturn(true)
        .thenThrow(new Exception("Deleting the same user twice"));

        when(userRepo.deleteUser(4))
        .thenThrow(new Exception("Attempting to delete nonexistent user"));

        when(userRepo.deleteUser(null))
        .thenThrow(new Exception("Handling invalid delete request to user repo"));

        return userRepo;
    }

    protected IRoleRepository createMockedRoleRepositoryObject() throws Exception {

        IRoleRepository roleRepo = super.createMockedRoleRepositoryObject();

        when(roleRepo.removeAllRolesFromUser(3))
        .thenReturn(true)
        .thenThrow(new Exception("Deleting roles to the same user twice"));

        when(roleRepo.removeAllRolesFromUser(4))
        .thenThrow(new Exception("Attempting to delete roles to nonexistent user"));

        when(roleRepo.removeAllRolesFromUser(null))
        .thenThrow(new Exception("Handling invalid delete request to role repo"));

        return roleRepo;
    }

    @Test
    public void testDeleteUser_Success() {

        try {
            IUserRepository userRepo = createMockedUserRepositoryObject();
            IRoleRepository roleRepo = createMockedRoleRepositoryObject();

            UsecaseDeleteOneUser usecase = new UsecaseDeleteOneUser(userRepo, roleRepo);
            usecase.setAuthUserId(1);
            usecase.setRefUserId(3);

            assertEquals(UsecaseDeleteOneUser.RESULT_USER_DELETED_SUCCESSFULLY, usecase.execute());
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testDeleteUser_NotAuthorised() {

        try {
            IUserRepository userRepo = createMockedUserRepositoryObject();
            IRoleRepository roleRepo = createMockedRoleRepositoryObject();

            UsecaseDeleteOneUser usecase = new UsecaseDeleteOneUser(userRepo, roleRepo);
            usecase.setAuthUserId(2);
            usecase.setRefUserId(3);

            assertEquals(UsecaseDeleteOneUser.RESULT_NOT_AUTHORISED, usecase.execute());
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testDeleteUser_UserDoesNotExist() {

        try {
            IUserRepository userRepo = createMockedUserRepositoryObject();
            IRoleRepository roleRepo = createMockedRoleRepositoryObject();

            UsecaseDeleteOneUser usecase = new UsecaseDeleteOneUser(userRepo, roleRepo);
            usecase.setAuthUserId(1);
            usecase.setRefUserId(4);

            assertEquals(UsecaseDeleteOneUser.RESULT_USER_DOES_NOT_EXIST, usecase.execute());
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testDeleteUser_BadInputData() {

        try {
            IUserRepository userRepo = createMockedUserRepositoryObject();
            IRoleRepository roleRepo = createMockedRoleRepositoryObject();

            UsecaseDeleteOneUser usecase;

            usecase = new UsecaseDeleteOneUser(userRepo, roleRepo);

            try {
                usecase.setAuthUserId(null);
            }
            catch(IllegalArgumentException e){
                assertEquals(e.getMessage(), "authUserId cannot be null");
            }

            try {
                usecase.setRefUserId(null);
            }
            catch(IllegalArgumentException e){
                assertEquals(e.getMessage(), "refUserId cannot be null");
            }

            try {
                usecase.execute();
            }
            catch(IllegalStateException e){
                assertEquals(e.getMessage(), "authUserId not provided");
            }

            usecase.setAuthUserId(1);

            try {
                usecase.execute();
            }
            catch(IllegalStateException e){
                assertEquals(e.getMessage(), "refUserId not provided");
            }
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }
}
