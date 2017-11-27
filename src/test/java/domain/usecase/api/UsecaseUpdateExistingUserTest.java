package domain.usecase.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import domain.entity.Role;
import org.junit.Test;

import domain.constraints.repository.IRoleRepository;
import domain.constraints.repository.IUserRepository;

import domain.entity.User;
import domain.usecase.UsecaseTest;

public class UsecaseUpdateExistingUserTest extends UsecaseTest {

    protected IUserRepository createMockedUserRepositoryObject() throws Exception {

        IUserRepository userRepo = super.createMockedUserRepositoryObject();

        when(userRepo.updateUser(admin))
        .thenReturn(true)
        .thenThrow(new Exception("Updating the same user twice"));

        when(userRepo.updateUser(user2))
        .thenReturn(true)
        .thenThrow(new Exception("Updating the same user twice"));

        when(userRepo.updateUser(new User(2,"admin", "admin", new Integer[] { 1, 2, 3, 4 })))
        .thenThrow(new Exception("Probable constraint violation, attempting to update username to a value already in existance"));

        when(userRepo.updateUser(new User(3,null, "pass2", new Integer[] { 1, 2 })))
        .thenThrow(new Exception("Handling invalid update request to user repo"));

        when(userRepo.updateUser(new User(3,"user2", "pass2", null)))
        .thenThrow(new Exception("Handling invalid update request to user repo"));

        return userRepo;
    }

    protected IRoleRepository createMockedRoleRepositoryObject() throws Exception {

        IRoleRepository roleRepo = super.createMockedRoleRepositoryObject();

        when(roleRepo.setRolesToUser(user2, new Role[] { role3 })).thenReturn(true)
        .thenThrow(new Exception("Updating the roles twice to the same user"));

        when(roleRepo.setRolesToUser(null, new Role[] { role1 }))
        .thenThrow(new Exception("Handling invalid insert request to role repo"));

        when(roleRepo.setRolesToUser(user2, null))
        .thenThrow(new Exception("Handling invalid insert request to role repo"));

        when(roleRepo.setRolesToUser(user3, new Role[] { role4 }))
        .thenThrow(new Exception("Inserting the roles to a user that does not exist"));

        when(roleRepo.removeAllRolesFromUser(user2.getId()))
        .thenReturn(true)
        .thenThrow(new Exception("Deleting the roles twice to the same user"));

        when(roleRepo.removeAllRolesFromUser(null))
        .thenThrow(new Exception("Handling invalid delete request to role repo"));

        return roleRepo;
    }

    @Test
    public void testUpdateUser_Success() {

        try {
            IUserRepository userRepo = createMockedUserRepositoryObject();
            IRoleRepository roleRepo = createMockedRoleRepositoryObject();

            UsecaseUpdateExistingUser usecase = new UsecaseUpdateExistingUser(userRepo, roleRepo);
            usecase.setAuthUserId(1);
            usecase.setRefUserId(3);
            usecase.setUserData(user2);

            assertEquals(UsecaseUpdateExistingUser.RESULT_USER_UPDATED_SUCCESSFULLY, usecase.execute());
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testUpdateUser_NotAuthorised() {

        try {
            IUserRepository userRepo = createMockedUserRepositoryObject();
            IRoleRepository roleRepo = createMockedRoleRepositoryObject();

            UsecaseUpdateExistingUser usecase = new UsecaseUpdateExistingUser(userRepo, roleRepo);
            usecase.setAuthUserId(2);
            usecase.setRefUserId(3);
            usecase.setUserData(user2);

            assertEquals(UsecaseUpdateExistingUser.RESULT_NOT_AUTHORISED, usecase.execute());
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testUpdateUser_UserDoesNotExist() {

        try {
            IUserRepository userRepo = createMockedUserRepositoryObject();
            IRoleRepository roleRepo = createMockedRoleRepositoryObject();

            UsecaseUpdateExistingUser usecase = new UsecaseUpdateExistingUser(userRepo, roleRepo);
            usecase.setAuthUserId(1);
            usecase.setRefUserId(4);
            usecase.setUserData(user3);

            assertEquals(UsecaseUpdateExistingUser.RESULT_USER_DOES_NOT_EXIST, usecase.execute());
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testUpdateUser_UsernameAlreadyTaken() {

        try {
            IUserRepository userRepo = createMockedUserRepositoryObject();
            IRoleRepository roleRepo = createMockedRoleRepositoryObject();

            UsecaseUpdateExistingUser usecase = new UsecaseUpdateExistingUser(userRepo, roleRepo);
            usecase.setAuthUserId(1);
            usecase.setRefUserId(2);
            usecase.setUserData(admin);

            assertEquals(UsecaseUpdateExistingUser.RESULT_USERNAME_ALREADY_TAKEN, usecase.execute());
        } 
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testUpdateUser_BadInputData() {

        try {
            IUserRepository userRepo = createMockedUserRepositoryObject();
            IRoleRepository roleRepo = createMockedRoleRepositoryObject();

            UsecaseUpdateExistingUser usecase;

            usecase = new UsecaseUpdateExistingUser(userRepo, roleRepo);

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
                usecase.setUserData(null);
            }
            catch(IllegalArgumentException e){
                assertEquals(e.getMessage(), "userData cannot be null");
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

            usecase.setRefUserId(3);

            try {
                usecase.execute();
            }
            catch(IllegalStateException e){
                assertEquals(e.getMessage(), "userData not provided");
            }

            usecase.setUserData(new User(3, null, "pass2", new Integer[] { 1, 2 }));
            assertEquals(UsecaseUpdateExistingUser.RESULT_BAD_INPUT_DATA, usecase.execute());

            usecase.setUserData(new User(3, "user2", null, new Integer[] { 1, 2 }));
            assertEquals(UsecaseUpdateExistingUser.RESULT_BAD_INPUT_DATA, usecase.execute());

            usecase.setUserData(new User(3, "user2", "pass2", null));
            assertEquals(UsecaseUpdateExistingUser.RESULT_BAD_INPUT_DATA, usecase.execute());
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }
}
