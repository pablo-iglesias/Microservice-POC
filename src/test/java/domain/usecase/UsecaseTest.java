package domain.usecase;

import static org.mockito.Mockito.when;

import org.mockito.Mockito;

import adapter.model.RoleModel;
import adapter.model.UserModel;

import domain.entity.Role;
import domain.entity.User;
import domain.factory.RoleFactory;
import domain.factory.UserFactory;

/**
 * All usecases work within an initial scenario where admin, user1 and user2 already exist, and user3 does not
 * Roles 1 to 4 do already exist, the application does not provide means to edit them, so they are immutable
 * Admin retains roles 1 to 4, user1 and user2 retain roles 2 and 3 respectively 
 * 
 * @author Pablo
 */
public class UsecaseTest {

	protected UserFactory createMockedUserFactoryObject() throws Exception{
		
		UserFactory factory = Mockito.mock(UserFactory.class);
		
		when(factory.create()).thenReturn(
				new User[]{ 
						new User(1, "admin", new Integer[]{1, 2, 3, 4}),
						new User(2, "user1", new Integer[]{2}),
						new User(3, "user2", new Integer[]{3})
				});
		
		when(factory.create("admin", "admin")).thenReturn(new User(1, "admin", new Integer[]{1, 2, 3, 4}));
        when(factory.create("user1", "pass1")).thenReturn(new User(2, "user1", new Integer[]{2}));
        when(factory.create("user2", "pass2")).thenReturn(new User(3, "user2", new Integer[]{3}));
        when(factory.create("user1", "pass2")).thenReturn(null);
        
        when(factory.create(1)).thenReturn(new User(1, "admin", new Integer[]{1, 2, 3, 4}));
        when(factory.create(2)).thenReturn(new User(2, "user1", new Integer[]{2}));
        when(factory.create(3)).thenReturn(new User(3, "user2", new Integer[]{3}));
        when(factory.create(4)).thenReturn(null);
        
        return factory;
	}
	
	protected RoleFactory createMockedRoleFactoryObject() throws Exception{
		
		RoleFactory factory = Mockito.mock(RoleFactory.class);
		
		when(factory.createByIds(new Integer[]{1, 2, 3, 4})).thenReturn(
				new Role[]{ 
						new Role(1, "ADMIN", ""),
						new Role(2, "PAGE_1", "page_1"),
						new Role(3, "PAGE_2", "page_2"),
						new Role(4, "PAGE_3", "page_3")
				});
		
		when(factory.createByIds(new Integer[]{2})).thenReturn(
				new Role[]{ 
						new Role(2, "PAGE_1", "page_1")
				});
		
		when(factory.createByIds(new Integer[]{3})).thenReturn(
				new Role[]{ 
						new Role(3, "PAGE_2", "page_2")
				});
        
        return factory;
	}
	
	protected UserModel createMockedUserModelObject() throws Exception{
		
		UserModel model = Mockito.mock(UserModel.class);
		
        when(model.selectUserIsAdminRole(1)).thenReturn(true);
        when(model.selectUserIsAdminRole(2)).thenReturn(false);
        when(model.selectUserIsAdminRole(3)).thenReturn(false);
        when(model.selectUserIsAdminRole(4)).thenReturn(false);
        
        when(model.selectUserExists(1)).thenReturn(true);
        when(model.selectUserExists(2)).thenReturn(true);
        when(model.selectUserExists(3)).thenReturn(true);
        when(model.selectUserExists(4)).thenReturn(false);
        
        when(model.selectUserExistsByUseraname("admin")).thenReturn(true);
        when(model.selectUserExistsByUseraname("user1")).thenReturn(true);
        when(model.selectUserExistsByUseraname("user2")).thenReturn(true);
        when(model.selectUserExistsByUseraname("user3")).thenReturn(false);
        
        when(model.selectUserIdByUseraname("admin")).thenReturn(1);
        when(model.selectUserIdByUseraname("user1")).thenReturn(2);
        when(model.selectUserIdByUseraname("user2")).thenReturn(3);
        when(model.selectUserIdByUseraname("user3")).thenReturn(null);
        
        return model;
	}
	
	protected RoleModel createMockedRoleModelObject() throws Exception{
		
		RoleModel model = Mockito.mock(RoleModel.class);
        
		when(model.getRoleIdsByUserId(1)).thenReturn(new Integer[]{1, 2, 3, 4});
		when(model.getRoleIdsByUserId(2)).thenReturn(new Integer[]{2});
		when(model.getRoleIdsByUserId(3)).thenReturn(new Integer[]{3});
		when(model.getRoleIdsByUserId(4)).thenReturn(new Integer[]{});
		
        return model;
	}
}
