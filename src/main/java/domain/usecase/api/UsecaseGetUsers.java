package domain.usecase.api;

import java.util.ArrayList;
import java.util.List;

import domain.contract.entity.RoleObject;
import domain.contract.entity.UserObject;

import domain.contract.repository.IRoleRepository;
import domain.contract.repository.IUserRepository;

import domain.usecase.Usecase;

import domain.entity.Role;
import domain.entity.User;

import javax.inject.Inject;

public class UsecaseGetUsers extends Usecase {

    public enum Result{
        USERS_RETRIEVED_SUCCESSFULLY,
        NO_USERS_FOUND
    }

    @Inject private IUserRepository userRepository;
    @Inject private IRoleRepository roleRepository;

    private User[] users = null;
    private Role[] roles = null;

    public UserObject[] getUsers() {
        return users;
    }

    public RoleObject[] getRoles() {
        return roles;
    }

    @Override
    public Result execute() throws Exception {

        users = userRepository.getAllUsers();

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

            return Result.USERS_RETRIEVED_SUCCESSFULLY;
        }

        return Result.NO_USERS_FOUND;
    }
}
