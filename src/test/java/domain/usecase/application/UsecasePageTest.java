package domain.usecase.application;

import static org.junit.Assert.*;

import org.junit.Test;

import domain.factory.RoleFactory;
import domain.factory.UserFactory;
import domain.usecase.UsecaseTest;

public class UsecasePageTest extends UsecaseTest{

	@Test
	public void pageTest_Success() {
		
		try{
	        
	        UserFactory userFactory = createMockedUserFactoryObject();
	        RoleFactory roleFactory = createMockedRoleFactoryObject();
			
	        UsecasePage usecase = new UsecasePage(userFactory, roleFactory);
	        usecase.uid = 2;
	        usecase.page = 1;
	        
	        assertEquals(true, usecase.execute() && usecase.allowed && usecase.username == "user1");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			fail(e.getMessage());
		}
	}
	
	@Test
	public void pageTest_Inexistant() {
		
		try{
	        
	        UserFactory userFactory = createMockedUserFactoryObject();
	        RoleFactory roleFactory = createMockedRoleFactoryObject();
			
	        UsecasePage usecase = new UsecasePage(userFactory, roleFactory);
	        usecase.uid = 4;
	        usecase.page = 1;
	        
	        assertEquals(false, usecase.execute());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			fail(e.getMessage());
		}
	}
	
	@Test
	public void pageTest_NotAuthorised() {
		
		try{
	        
	        UserFactory userFactory = createMockedUserFactoryObject();
	        RoleFactory roleFactory = createMockedRoleFactoryObject();
			
	        UsecasePage usecase = new UsecasePage(userFactory, roleFactory);
	        usecase.uid = 3;
	        usecase.page = 1;
	        
	        assertEquals(true, usecase.execute() && usecase.allowed == false);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			fail(e.getMessage());
		}
	}
}
