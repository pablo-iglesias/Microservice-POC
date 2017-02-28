package domain.usecase.api;

import java.util.Vector;

import adapter.model.RoleModel;
import adapter.model.UserModel;

import domain.entity.Role;
import domain.entity.User;
import domain.factory.RoleFactory;
import domain.factory.UserFactory;
import domain.usecase.Usecase;

public class UsecaseGetUsers extends Usecase {

    // Factory
    private UserFactory userFactory;
    private RoleFactory roleFactory;

    // Output data
    public User[] users = null;
    public Role[] roles = null;

    public UsecaseGetUsers(UserModel userModel, RoleModel roleModel) throws Exception {
        userFactory = new UserFactory(userModel, roleModel);
        roleFactory = new RoleFactory(roleModel);
    }

    public UsecaseGetUsers(UserFactory userFactory, RoleFactory roleFactory) {
        this.userFactory = userFactory;
        this.roleFactory = roleFactory;
    }

    public boolean execute() throws Exception {

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

            return true;
        }

        return false;
    }
}
