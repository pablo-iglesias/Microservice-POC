package domain.usecase.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import domain.entity.Role;
import org.junit.Test;

import domain.constraints.repository.IRoleRepository;
import domain.constraints.repository.IUserRepository;

import domain.entity.User;
import domain.usecase.UsecaseTest;

public class UsecaseAddNewUserTest extends UsecaseTest {

    protected IUserRepository createMockedUserRepositoryObject() throws Exception {

        IUserRepository userRepo = super.createMockedUserRepositoryObject();

        when(userRepo.insertUser(user3))
        .thenReturn(4)
        .thenThrow(new Exception("Creating the same user twice"));

        return userRepo;
    }

    protected IRoleRepository createMockedRoleRepositoryObject() throws Exception {

        IRoleRepository roleRepo = super.createMockedRoleRepositoryObject();

        when(roleRepo.setRolesToUser(user3, new Role[] { role4 }))
        .thenReturn(true)
        .thenThrow(new Exception("Adding the same roles twice to the user"));

        when(roleRepo.setRolesToUser(admin, new Role[] { role4 }))
        .thenThrow(new Exception("Adding the roles to a preexisting user"));

        when(roleRepo.setRolesToUser(user1, new Role[] { role4 }))
        .thenThrow(new Exception("Adding the roles to a preexisting user"));

        when(roleRepo.setRolesToUser(user2, new Role[] { role4 }))
        .thenThrow(new Exception("Adding the roles to a preexisting user"));

        return roleRepo;
    }

    @Test
    public void testAddNewUser_Success() {

        try {
            IUserRepository userRepository = createMockedUserRepositoryObject();
            IRoleRepository roleRepository = createMockedRoleRepositoryObject();

            UsecaseAddNewUser usecase = new UsecaseAddNewUser(userRepository, roleRepository);
            usecase.setAuthUserId(1);
            usecase.setUserData(user3);

            assertEquals(UsecaseAddNewUser.RESULT_USER_CREATED_SUCCESSFULLY, usecase.execute());
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testAddNewUser_NotAuthorised() {

        try {
            IUserRepository userRepository = createMockedUserRepositoryObject();
            IRoleRepository roleRepository = createMockedRoleRepositoryObject();

            UsecaseAddNewUser usecase = new UsecaseAddNewUser(userRepository, roleRepository);
            usecase.setAuthUserId(2);
            usecase.setUserData(user3);

            assertEquals(UsecaseAddNewUser.RESULT_NOT_AUTHORISED, usecase.execute());
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testAddNewUser_UserAlreadyExists() {

        try {
            IUserRepository userRepository = createMockedUserRepositoryObject();
            IRoleRepository roleRepository = createMockedRoleRepositoryObject();

            UsecaseAddNewUser usecase = new UsecaseAddNewUser(userRepository, roleRepository);
            usecase.setAuthUserId(1);
            usecase.setUserData(admin);

            assertEquals(UsecaseAddNewUser.RESULT_USER_ALREADY_EXISTS, usecase.execute());
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }

    @Test
    public void testAddNewUser_BadInputData() {

        try {
            IUserRepository userRepository = createMockedUserRepositoryObject();
            IRoleRepository roleRepository = createMockedRoleRepositoryObject();

            UsecaseAddNewUser usecase = new UsecaseAddNewUser(userRepository, roleRepository);

            try {
                usecase.setAuthUserId(null);
            }
            catch(IllegalArgumentException e){
                assertEquals(e.getMessage(), "authUserId cannot be null");
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
                assertEquals(e.getMessage(), "userData not provided");
            }

            usecase.setUserData(new User(0, "", "", new Integer[0]));

            assertEquals(UsecaseAddNewUser.RESULT_BAD_INPUT_DATA, usecase.execute());
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e.getMessage());
        }
    }
}
