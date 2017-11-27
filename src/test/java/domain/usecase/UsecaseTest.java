package domain.usecase;

import static org.mockito.Mockito.when;

import domain.Helper;
import domain.constraints.UserObject;
import org.mockito.Mockito;

import domain.constraints.repository.IRoleRepository;
import domain.constraints.repository.IUserRepository;

import domain.entity.Role;
import domain.entity.User;

/**
 * All usecases work within an initial scenario where admin, user1 and user2 already exist, and user3 does not 
 * Roles 1 to 4 do already exist, the application does not provide means to edit them, so they are immutable
 * Admin retains roles 1 to 4, user1 and user2 retain roles 2 and 3 respectively
 * 
 * @author Pablo
 */
public class UsecaseTest {

    protected final User admin = new User(1, "admin", "admin", new Integer[] { 1, 2, 3, 4 });
    protected final User user1 = new User(2, "user1", "pass1", new Integer[] { 2 });
    protected final User user2 = new User(3, "user2", "pass2", new Integer[] { 3 });
    protected final User user3 = new User(4, "user3", "pass3", new Integer[] { 4 } );

    protected final Role role1 = new Role(1, "ADMIN", "");
    protected final Role role2 = new Role(2, "PAGE_1", "page_1");
    protected final Role role3 = new Role(3, "PAGE_2", "page_2");
    protected final Role role4 = new Role(4, "PAGE_3", "page_3");

    protected IUserRepository createMockedUserRepositoryObject() throws Exception {

        IUserRepository userRepo = Mockito.mock(IUserRepository.class);

        when(userRepo.getAllUsers()).thenReturn(new User[] {admin, user1, user2});

        when(userRepo.getUser(1)).thenReturn(admin);
        when(userRepo.getUser(2)).thenReturn(user1);
        when(userRepo.getUser(3)).thenReturn(user2);

        when(userRepo.getUser(4)).thenReturn(null);

        when(userRepo.getUser("admin")).thenReturn(admin);
        when(userRepo.getUser("user1")).thenReturn(user1);
        when(userRepo.getUser("user2")).thenReturn(user2);

        when(userRepo.getUser("user3")).thenReturn(null);

        when(userRepo.getUser("admin", Helper.SHA1("admin"))).thenReturn(admin);
        when(userRepo.getUser("user1", Helper.SHA1("pass1"))).thenReturn(user1);
        when(userRepo.getUser("user2", Helper.SHA1("pass2"))).thenReturn(user2);

        when(userRepo.getUser("user1", Helper.SHA1("pass2"))).thenReturn(null);

        return userRepo;
    }

    protected IRoleRepository createMockedRoleRepositoryObject() throws Exception {

        IRoleRepository roleRepo = Mockito.mock(IRoleRepository.class);
        when(roleRepo.getRolesByUser(admin)).thenReturn(new Role[] { role1, role2, role3, role4 });
        when(roleRepo.getRolesByUser(user1)).thenReturn(new Role[] { role2 });
        when(roleRepo.getRolesByUser(user2)).thenReturn(new Role[] { role3 });
        return roleRepo;
    }
}
