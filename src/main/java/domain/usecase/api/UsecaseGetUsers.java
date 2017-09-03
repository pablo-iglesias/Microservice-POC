package domain.usecase.api;

import java.util.Vector;

import domain.usecase.Usecase;

import domain.entity.Role;
import domain.entity.User;

import domain.model.RoleModel;
import domain.model.UserModel;

import domain.factory.RoleFactory;
import domain.factory.UserFactory;

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
    public User[] getUsers() {
        return users;
    }

    public Role[] getRoles() {
        return roles;
    }

    // Constructor
    public UsecaseGetUsers(UserModel userModel, RoleModel roleModel) throws Exception {
        userFactory = new UserFactory(userModel, roleModel);
        roleFactory = new RoleFactory(roleModel);
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
