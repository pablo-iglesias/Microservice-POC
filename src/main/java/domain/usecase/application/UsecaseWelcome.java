package domain.usecase.application;

import adapter.model.RoleModel;
import adapter.model.UserModel;

import domain.entity.User;
import domain.entity.Role;
import domain.factory.RoleFactory;
import domain.factory.UserFactory;
import domain.usecase.Usecase;

public class UsecaseWelcome extends Usecase {

    public static final int ROLE_ID = 0;
    public static final int ROLE_NAME = 1;

    // Factory
    private UserFactory userFactory;
    private RoleFactory roleFactory;

    // Input data
    public int uid = 0;

    // Output data
    public String username = "";
    public Role[] roles = new Role[] {};

    public UsecaseWelcome(UserModel userModel, RoleModel roleModel) throws Exception {
        userFactory = new UserFactory(userModel, roleModel);
        roleFactory = new RoleFactory(roleModel);
    }

    public UsecaseWelcome(UserFactory userFactory, RoleFactory roleFactory) {
        this.userFactory = userFactory;
        this.roleFactory = roleFactory;
    }

    public boolean execute() throws Exception {

        User user = userFactory.create(uid);

        if (user != null) {
            username = user.getUsername();
            roles = roleFactory.createByIds(user.getRoles());

            return true;
        }

        return false;
    }
}
