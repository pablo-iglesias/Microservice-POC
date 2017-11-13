package domain.usecase.api;

import domain.usecase.Usecase;

import domain.entity.Role;
import domain.entity.User;

import domain.model.UserModel;
import domain.model.RoleModel;

import adapter.repository.RoleRepository;
import adapter.repository.UserRepository;

import domain.entity.factory.RoleFactory;
import domain.entity.factory.UserFactory;

public class UsecaseGetOneUser extends Usecase {

    public static final int RESULT_USER_RETRIEVED_SUCCESSFULLY = 1;
    public static final int RESULT_USER_NOT_FOUND = 2;

    // Factory
    private UserFactory userFactory;
    private RoleFactory roleFactory;

    // Input data
    private Integer refUserId = null;

    // Output data
    private User user = null;
    private Role[] roles = null;

    // Getter & Setter
    public Integer getRefUserId() {
        return refUserId;
    }

    public void setRefUserId(Integer refUserId) {
        if (refUserId == null) {
            throw new IllegalArgumentException("refUserId cannot be null");
        }

        this.refUserId = refUserId;
    }

    public UserModel getUser() {
        return user;
    }

    public RoleModel[] getRoles() {
        return roles;
    }

    // Constructor
    public UsecaseGetOneUser(UserRepository userRepository, RoleRepository roleRepository) throws Exception {
        userFactory = new UserFactory(userRepository, roleRepository);
        roleFactory = new RoleFactory(roleRepository);
    }

    public UsecaseGetOneUser(UserFactory userFactory, RoleFactory roleFactory) {
        this.userFactory = userFactory;
        this.roleFactory = roleFactory;
    }

    // Business Logic
    public int execute() throws Exception {

        if (refUserId == null) {
            throw new IllegalStateException("refUserId not provided");
        }

        user = userFactory.create(refUserId);

        if (user == null) {
            return RESULT_USER_NOT_FOUND;
        }

        roles = roleFactory.createByIds(user.getRoles());
        return RESULT_USER_RETRIEVED_SUCCESSFULLY;
    }
}