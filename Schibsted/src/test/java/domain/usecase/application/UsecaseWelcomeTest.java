package domain.usecase.application;

import static org.junit.Assert.*;

import org.junit.Test;

import domain.entity.Role;
import domain.factory.RoleFactory;
import domain.factory.UserFactory;
import domain.usecase.UsecaseTest;

public class UsecaseWelcomeTest extends UsecaseTest{

	@Test
	public void testWelcome_Success() {
		
		try{
	        
	        UserFactory userFactory = createMockedUserFactoryObject();
	        RoleFactory roleFactory = createMockedRoleFactoryObject();
			
	        UsecaseWelcome usecase = new UsecaseWelcome(userFactory, roleFactory);
	        usecase.uid = 1;
	        
	        assertEquals(true, usecase.execute() && usecase.username == "admin");
	        
	        assertEquals(new Role(1, "ADMIN", ""), usecase.roles[0]);
	        assertEquals(new Role(2, "PAGE_1", "page_1"), usecase.roles[1]);
	        assertEquals(new Role(3, "PAGE_2", "page_2"), usecase.roles[2]);
	        assertEquals(new Role(4, "PAGE_3", "page_3"), usecase.roles[3]);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testWelcome_Inexistant() {
		
		try{
	        
	        UserFactory userFactory = createMockedUserFactoryObject();
	        RoleFactory roleFactory = createMockedRoleFactoryObject();
			
	        UsecaseWelcome usecase = new UsecaseWelcome(userFactory, roleFactory);
	        usecase.uid = 4;
	        
	        assertEquals(false, usecase.execute());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			fail(e.getMessage());
		}
	}
}