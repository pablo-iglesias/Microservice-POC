package domain.usecase;

import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import domain.Helper;
import domain.constraints.UserObject;
import org.mockito.Mockito;

import domain.constraints.repository.IRoleRepository;
import domain.constraints.repository.IUserRepository;

import domain.entity.Role;
import domain.entity.User;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;

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

        Answer predicate = (InvocationOnMock i) -> {
            User user = (User) i.getArguments()[0];
            user.copyFrom(admin);
            return true;
        };

        Gson gson = new Gson();
        UserObject newAdmin = gson.fromJson("{username: 'admin', password: 'admin', roles: [1,2,3,4]}", UserObject.class);

        when(userRepo.findUser(new User(newAdmin))).then(predicate);
        when(userRepo.findUser(new User(1))).then(predicate);

        when(userRepo.findUser(new User(2)))
            .then( (InvocationOnMock i) -> {
                User user = (User) i.getArguments()[0];
                user.copyFrom(user1);
                return true;
            }
        );

        when(userRepo.findUser(new User(3)))
            .then( (InvocationOnMock i) -> {
                User user = (User) i.getArguments()[0];
                user.copyFrom(user2);
                return true;
            }
        );

        when(userRepo.findUser(new User(4))).thenReturn(false);

        when(userRepo.findUser(new User().setUsername("admin")))
            .then( (InvocationOnMock i) -> {
                User user = (User) i.getArguments()[0];
                user.copyFrom(admin);
                return true;
            }
        );

        when(userRepo.findUser(new User().setUsername("user1")))
            .then( (InvocationOnMock i) -> {
                User user = (User) i.getArguments()[0];
                user.copyFrom(user1);
                return true;
            }
        );

        when(userRepo.findUser(new User().setUsername("user2")))
            .then( (InvocationOnMock i) -> {
                User user = (User) i.getArguments()[0];
                user.copyFrom(user2);
                return true;
            }
        );

        when(userRepo.findUser(new User().setUsername("user3"))).thenReturn(false);

        when(userRepo.findUser(new User().setUsername("admin").setPassword("admin")))
            .then( (InvocationOnMock i) -> {
                User user = (User) i.getArguments()[0];
                user.copyFrom(admin);
                return true;
            }
        );

        when(userRepo.findUser(new User().setUsername("user1").setPassword("pass1")))
            .then( (InvocationOnMock i) -> {
                User user = (User) i.getArguments()[0];
                user.copyFrom(user1);
                return true;
            }
        );

        when(userRepo.findUser(new User().setUsername("user2").setPassword("pass2")))
            .then( (InvocationOnMock i) -> {
                User user = (User) i.getArguments()[0];
                user.copyFrom(user2);
                return true;
            }
        );

        when(userRepo.findUser(new User().setUsername("user1").setPassword("pass2"))).thenReturn(false);

        return userRepo;
    }

    protected IRoleRepository createMockedRoleRepositoryObject() throws Exception {

        IRoleRepository roleRepo = Mockito.mock(IRoleRepository.class);
        when(roleRepo.getRolesByUser(admin)).thenReturn(new Role[] { role1, role2, role3, role4 });
        when(roleRepo.getRolesByUser(new User(1))).thenReturn(new Role[] { role1, role2, role3, role4 });
        when(roleRepo.getRolesByUser(new User(2))).thenReturn(new Role[] { role2 });
        when(roleRepo.getRolesByUser(new User(3))).thenReturn(new Role[] { role3 });
        return roleRepo;
    }
}
