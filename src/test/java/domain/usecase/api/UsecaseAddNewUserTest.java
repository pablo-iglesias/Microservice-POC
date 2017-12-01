package domain.usecase.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import domain.constraints.UserObject;
import org.junit.Test;

import domain.constraints.repository.IRoleRepository;
import domain.constraints.repository.IUserRepository;

import domain.entity.User;
import domain.usecase.UsecaseTest;

import com.google.gson.Gson;

public class UsecaseAddNewUserTest extends UsecaseTest {

    protected IUserRepository createMockedUserRepositoryObject() throws Exception {

        IUserRepository userRepo = super.createMockedUserRepositoryObject();

        when(userRepo.insertUser(user3))
        .thenReturn(4)
        .thenThrow(new Exception("Creating the same user twice"));

        return userRepo;
    }

    @Test
    public void testAddNewUser_Success() {

        try {
            IUserRepository userRepository = createMockedUserRepositoryObject();
            IRoleRepository roleRepository = createMockedRoleRepositoryObject();

            UsecaseAddNewUser usecase = new UsecaseAddNewUser(userRepository, roleRepository);
            usecase.setAuthUserId(1);

            Gson gson = new Gson();
            UserObject user = gson.fromJson("{username: 'user3', password: 'pass3', roles: [4]}", UserObject.class);

            usecase.setUserData(user);

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

            Gson gson = new Gson();
            UserObject user = gson.fromJson("{username: 'user3', password: 'pass3', roles: [4]}", UserObject.class);

            usecase.setUserData(user);

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

            Gson gson = new Gson();
            UserObject user = gson.fromJson("{username: 'admin', password: 'admin', roles: [1,2,3,4]}", UserObject.class);

            usecase.setUserData(user);

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
