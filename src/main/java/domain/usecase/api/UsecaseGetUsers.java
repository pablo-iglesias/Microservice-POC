package domain.usecase.api;

import java.util.ArrayList;
import java.util.List;

import domain.constraints.repository.IRoleRepository;
import domain.constraints.repository.IUserRepository;
import domain.service.UserService;
import domain.usecase.Usecase;

import domain.entity.Role;
import domain.entity.User;

import domain.constraints.RoleObject;
import domain.constraints.UserObject;

public class UsecaseGetUsers extends Usecase {

    public static final int RESULT_USERS_RETRIEVED_SUCCESSFULLY = 1;
    public static final int RESULT_NO_USERS_FOUND = 2;

    private UserService service;
    private IRoleRepository roleRepository;

    // Output data
    private User[] users = null;
    private Role[] roles = null;

    // Getter & Setter
    public UserObject[] getUsers() {
        return users;
    }

    public RoleObject[] getRoles() {
        return roles;
    }

    // Constructor
    public UsecaseGetUsers(IUserRepository userRepository, IRoleRepository roleRepository) throws Exception {
        this.roleRepository = roleRepository;
        service = new UserService(userRepository, roleRepository);
    }

    // Business Logic
    public int execute() throws Exception {

        users = service.getAllUsers();

        if (users != null) {

            List<Role> roles = new ArrayList<>();
            for (User user : users) {
                Role[] userRoles = roleRepository.getRolesByUser(user);
                if(userRoles != null) {
                    for (Role role : userRoles) {
                        if (!roles.contains(role))
                            roles.add(role);
                    }
                }
            }

            this.roles = roles.stream().toArray(size -> new Role[size]);

            return RESULT_USERS_RETRIEVED_SUCCESSFULLY;
        }

        return RESULT_NO_USERS_FOUND;
    }
}
