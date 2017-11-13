package domain.usecase.api;

import java.util.Vector;

import domain.model.RoleModel;
import domain.model.UserModel;
import domain.usecase.Usecase;

import domain.entity.Role;
import domain.entity.User;

import adapter.repository.RoleRepository;
import adapter.repository.UserRepository;

import domain.entity.factory.RoleFactory;
import domain.entity.factory.UserFactory;

public class UsecaseGetUsers extends Usecase {

    public static final int RESULT_USERS_RETRIEVED_SUCCESSFULLY = 1;
    public static final int RESULT_NO_USERS_FOUND = 2;

    // Factory
    private UserFactory userFactory;
    private RoleFactory roleFactory;

    // Output data
    private User[] users = null;
    private Role[] roles = null;

    // Getter & Setter
    public UserModel[] getUsers() {
        return users;
    }

    public RoleModel[] getRoles() {
        return roles;
    }

    // Constructor
    public UsecaseGetUsers(UserRepository userRepository, RoleRepository roleRepository) throws Exception {
        userFactory = new UserFactory(userRepository, roleRepository);
        roleFactory = new RoleFactory(roleRepository);
    }

    public UsecaseGetUsers(UserFactory userFactory, RoleFactory roleFactory) {
        this.userFactory = userFactory;
        this.roleFactory = roleFactory;
    }

    // Business Logic
    public int execute() throws Exception {

        users = userFactory.create();

        if (users != null) {

            Vector<Integer> rolesVector = new Vector<Integer>();

            for (User user : users) {
                for (int role : user.getRoles()) {
                    if (!rolesVector.contains(role)) {
                        rolesVector.add(role);
                    }
                }
            }

            Integer[] rolesArray = (Integer[]) rolesVector.toArray(new Integer[0]);

            roles = roleFactory.createByIds(rolesArray);

            return RESULT_USERS_RETRIEVED_SUCCESSFULLY;
        }

        return RESULT_NO_USERS_FOUND;
    }
}
