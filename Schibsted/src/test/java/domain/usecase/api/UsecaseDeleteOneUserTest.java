package domain.usecase.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.junit.Test;

import adapter.model.generic.RoleModel;
import adapter.model.generic.UserModel;
import domain.usecase.UsecaseTest;

public class UsecaseDeleteOneUserTest extends UsecaseTest {
	
	protected UserModel createMockedUserModelObject() throws Exception{
		
		UserModel model = super.createMockedUserModelObject();
		
		when(
        	model.deleteUser(3)
        )
        .thenReturn(true)
        .thenThrow(new Exception("Deleting the same user twice"));
		
		when(
        	model.deleteUser(4)
        )
        .thenThrow(new Exception("Attempting to delete unexistant user"));
		
		when(
        	model.deleteUser(null)
        )
        .thenThrow(new Exception("Handling invalid delete request to user model"));
		
		return model;
	}

	protected RoleModel createMockedRoleModelObject() throws Exception{
		
		RoleModel model = super.createMockedRoleModelObject();
		
		when(
        	model.deleteUserHasRolesByUserId(3)
        )
        .thenReturn(true)
        .thenThrow(new Exception("Deleting roles to the same user twice"));
		
		when(
        	model.deleteUserHasRolesByUserId(4)
        )
        .thenThrow(new Exception("Attempting to delete roles to unexistant user"));
		
		when(
        	model.deleteUserHasRolesByUserId(null)
        )
        .thenThrow(new Exception("Handling invalid delete request to role model"));
        
        return model;
	}

	@Test
	public void testDeleteUser_Success() {
		
		try{
			UserModel userModel = createMockedUserModelObject();
			RoleModel roleModel = createMockedRoleModelObject();
			
			UsecaseDeleteOneUser usecase = new UsecaseDeleteOneUser(userModel, roleModel);
			usecase.authUserId = 1;
			usecase.refUserId = 3;
			
			assertEquals(UsecaseDeleteOneUser.RESULT_USER_DELETED_SUCCESSFULLY, usecase.execute());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testDeleteUser_NotAuthorised() {
		
		try{
			UserModel userModel = createMockedUserModelObject();
			RoleModel roleModel = createMockedRoleModelObject();
			
			UsecaseDeleteOneUser usecase = new UsecaseDeleteOneUser(userModel, roleModel);
			usecase.authUserId = 2;
			usecase.refUserId = 3;
			
			assertEquals(UsecaseDeleteOneUser.RESULT_NOT_AUTHORISED, usecase.execute());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testDeleteUser_UserDoesNotExist() {
		
		try{
			UserModel userModel = createMockedUserModelObject();
			RoleModel roleModel = createMockedRoleModelObject();
			
			UsecaseDeleteOneUser usecase = new UsecaseDeleteOneUser(userModel, roleModel);
			usecase.authUserId = 1;
			usecase.refUserId = 4;
			
			assertEquals(UsecaseDeleteOneUser.RESULT_USER_DOES_NOT_EXIST, usecase.execute());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testDeleteUser_BadInputData() {
		
		try{
			UserModel userModel = createMockedUserModelObject();
			RoleModel roleModel = createMockedRoleModelObject();
			
			UsecaseDeleteOneUser usecase;
			
			usecase = new UsecaseDeleteOneUser(userModel, roleModel);
			usecase.authUserId = null;
			usecase.refUserId = 3;
			
			assertEquals(UsecaseDeleteOneUser.RESULT_BAD_INPUT_DATA, usecase.execute());
			
			usecase = new UsecaseDeleteOneUser(userModel, roleModel);
			usecase.authUserId = 1;
			usecase.refUserId = null;
			
			assertEquals(UsecaseDeleteOneUser.RESULT_BAD_INPUT_DATA, usecase.execute());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			fail(e.getMessage());
		}
	}
}
