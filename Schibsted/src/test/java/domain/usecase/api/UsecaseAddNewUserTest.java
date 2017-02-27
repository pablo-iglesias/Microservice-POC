package domain.usecase.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.junit.Test;

import adapter.model.RoleModel;
import adapter.model.UserModel;

import domain.Helper;
import domain.entity.User;
import domain.usecase.UsecaseTest;

public class UsecaseAddNewUserTest extends UsecaseTest {

	protected UserModel createMockedUserModelObject() throws Exception{
		
		UserModel model = super.createMockedUserModelObject();
		
		when(
        	model.insertUser("user3", Helper.SHA1("pass3"))
        )
        .thenReturn(4)
        .thenThrow(new Exception("Inserting the same user twice"));
                
        when(
        	model.insertUser("admin", Helper.SHA1("admin"))
        )
        .thenThrow(new Exception("Probable constraint violation, attempting to insert username with a value already in existance"));
        
        when(
        	model.insertUser("user1", Helper.SHA1("pass1"))
        )
        .thenThrow(new Exception("Probable constraint violation, attempting to insert username with a value already in existance"));
        
        when(
        	model.insertUser("user2", Helper.SHA1("pass2"))
        )
        .thenThrow(new Exception("Probable constraint violation, attempting to insert username with a value already in existance"));
		
		return model;
	}

	protected RoleModel createMockedRoleModelObject() throws Exception{
		
		RoleModel model = super.createMockedRoleModelObject();
		
		when(
        	model.insertUserHasRoles(4, new Integer[]{4})
        )
        .thenReturn(true)
        .thenThrow(new Exception("Adding the same roles twice to the user"));
		
        when(
        	model.insertUserHasRoles(1, new Integer[]{4})
        )
        .thenThrow(new Exception("Adding the roles to a preexisting user"));
        
        when(
        	model.insertUserHasRoles(2, new Integer[]{4})
        )
        .thenThrow(new Exception("Adding the roles to a preexisting user"));
        
        when(
        	model.insertUserHasRoles(3, new Integer[]{4})
        )
        .thenThrow(new Exception("Adding the roles to a preexisting user"));
                
        return model;
	}
	
	@Test
	public void testAddNewUser_Success() {
		
		try{
			UserModel userModel = createMockedUserModelObject();
			RoleModel roleModel = createMockedRoleModelObject();
			
			UsecaseAddNewUser usecase = new UsecaseAddNewUser(userModel, roleModel);
			usecase.uid = 1;
			usecase.user = new User(0, "user3", "pass3", new Integer[]{4});
			
			assertEquals(UsecaseAddNewUser.RESULT_USER_CREATED_SUCCESSFULLY, usecase.execute());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testAddNewUser_NotAuthorised() {
		
		try{
			UserModel userModel = createMockedUserModelObject();
			RoleModel roleModel = createMockedRoleModelObject();
			
			UsecaseAddNewUser usecase = new UsecaseAddNewUser(userModel, roleModel);
			usecase.uid = 2;
			usecase.user = new User(0, "user3", "pass3", new Integer[]{4});
			
			assertEquals(UsecaseAddNewUser.RESULT_NOT_AUTHORISED, usecase.execute());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testAddNewUser_UserAlreadyExists() {
		
		try{
			UserModel userModel = createMockedUserModelObject();
			RoleModel roleModel = createMockedRoleModelObject();
			
			UsecaseAddNewUser usecase = new UsecaseAddNewUser(userModel, roleModel);
			usecase.uid = 1;
			usecase.user = new User(0, "admin", "admin", new Integer[]{4});
			
			assertEquals(UsecaseAddNewUser.RESULT_USER_ALREADY_EXISTS, usecase.execute());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testAddNewUser_BadInputData() {
		
		try{
			UserModel userModel = createMockedUserModelObject();
			RoleModel roleModel = createMockedRoleModelObject();
			
			UsecaseAddNewUser usecase = new UsecaseAddNewUser(userModel, roleModel);
			usecase.uid = null;
			usecase.user = null;
			
			assertEquals(UsecaseAddNewUser.RESULT_BAD_INPUT_DATA, usecase.execute());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			fail(e.getMessage());
		}
	}
}
