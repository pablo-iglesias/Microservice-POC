package domain.usecase;

import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import domain.service.UserService;
import domain.contract.entity.UserObject;
import domain.contract.repository.IRoleRepository;
import domain.contract.repository.IUserRepository;
import domain.entity.Role;
import domain.entity.User;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

/**
 * All usecases work within an initial scenario where admin, user1 and user2 already exist, and user3 does not
 * Roles 1 to 4 do already exist, the application does not provide means to edit them, so they are immutable
 * Admin retains roles 1 to 4, user1 and user2 retain roles 2 and 3 respectively
 *
 * @author Pablo
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class UsecaseTest {

    protected final User admin = new User(1, "admin", "admin", new Integer[] { 1, 2, 3, 4 });
    protected final User user1 = new User(2, "user1", "pass1", new Integer[] { 2 });
    protected final User user2 = new User(3, "user2", "pass2", new Integer[] { 3 });
    protected final User user3 = new User(4, "user3", "pass3", new Integer[] { 4 } );

    protected final Role role1 = new Role(1, "ADMIN", "");
    protected final Role role2 = new Role(2, "PAGE_1", "page_1");
    protected final Role role3 = new Role(3, "PAGE_2", "page_2");
    protected final Role role4 = new Role(4, "PAGE_3", "page_3");

    @Mock protected UserService service;
    @Mock protected IUserRepository userRepository;
    @Mock protected IRoleRepository roleRepository;

    @Before
    public void initMocks() throws Exception{
        initUserServiceMock();
        initUserRepositoryMock();
        initRoleRepositoryMock();
    }

    public class UserObject implements domain.contract.entity.UserObject {

        private Integer id;
        private String username;
        private String password;
        private Integer[] roles;

        public Integer getId() { return id; }
        public String getUsername() { return username; }
        public Integer[] getRoleIds() { return roles; }
        public String getPassword() { return password; }
    }

    protected void initUserServiceMock() throws Exception {

        when(service.isUserAnAdmin(new User(1))).thenReturn(true);
        when(service.isUserAnAdmin(new User(2))).thenReturn(false);
        when(service.isUserAnAdmin(new User(3))).thenReturn(false);
        when(service.isUserAnAdmin(new User(4))).thenReturn(false);

        when(service.getUserNameByUserId(1)).thenReturn("admin");
        when(service.getUserNameByUserId(2)).thenReturn("user1");
        when(service.getUserNameByUserId(3)).thenReturn("user2");
        when(service.getUserNameByUserId(4)).thenReturn("user3");
    }


    protected void initUserRepositoryMock() throws Exception {

        when(userRepository.getAllUsers()).thenReturn(new User[] {admin, user1, user2});

        Answer predicate = (InvocationOnMock i) -> {
            User user = (User) i.getArguments()[0];
            user.copyFrom(admin);
            return true;
        };

        Gson gson = new Gson();
        UserObject newAdmin = gson.fromJson("{username: 'admin', password: 'admin', roles: [1,2,3,4]}", UserObject.class);

        when(userRepository.findUser(new User(newAdmin))).then(predicate);
        when(userRepository.findUser(new User(1))).then(predicate);

        when(userRepository.findUser(new User(2)))
            .then( (InvocationOnMock i) -> {
                User user = (User) i.getArguments()[0];
                user.copyFrom(user1);
                return true;
            }
        );

        when(userRepository.findUser(new User(3)))
            .then( (InvocationOnMock i) -> {
                User user = (User) i.getArguments()[0];
                user.copyFrom(user2);
                return true;
            }
        );

        when(userRepository.findUser(new User(4))).thenReturn(false);

        when(userRepository.findUser(new User().setUsername("admin")))
            .then( (InvocationOnMock i) -> {
                User user = (User) i.getArguments()[0];
                user.copyFrom(admin);
                return true;
            }
        );

        when(userRepository.findUser(new User().setUsername("user1")))
            .then( (InvocationOnMock i) -> {
                User user = (User) i.getArguments()[0];
                user.copyFrom(user1);
                return true;
            }
        );

        when(userRepository.findUser(new User().setUsername("user2")))
            .then( (InvocationOnMock i) -> {
                User user = (User) i.getArguments()[0];
                user.copyFrom(user2);
                return true;
            }
        );

        when(userRepository.findUser(new User().setUsername("user3"))).thenReturn(false);

        when(userRepository.findUser(new User().setUsername("admin").setPassword("admin")))
            .then( (InvocationOnMock i) -> {
                User user = (User) i.getArguments()[0];
                user.copyFrom(admin);
                return true;
            }
        );

        when(userRepository.findUser(new User().setUsername("user1").setPassword("pass1")))
            .then( (InvocationOnMock i) -> {
                User user = (User) i.getArguments()[0];
                user.copyFrom(user1);
                return true;
            }
        );

        when(userRepository.findUser(new User().setUsername("user2").setPassword("pass2")))
            .then( (InvocationOnMock i) -> {
                User user = (User) i.getArguments()[0];
                user.copyFrom(user2);
                return true;
            }
        );

        when(userRepository.findUser(new User().setUsername("user1").setPassword("pass2"))).thenReturn(false);
    }

    protected void initRoleRepositoryMock() throws Exception {

        when(roleRepository.getRolesByUser(admin)).thenReturn(new Role[] { role1, role2, role3, role4 });
        when(roleRepository.getRolesByUser(new User(1))).thenReturn(new Role[] { role1, role2, role3, role4 });
        when(roleRepository.getRolesByUser(new User(2))).thenReturn(new Role[] { role2 });
        when(roleRepository.getRolesByUser(new User(3))).thenReturn(new Role[] { role3 });
    }
}
