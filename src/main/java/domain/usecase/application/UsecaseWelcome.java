package domain.usecase.application;

import domain.constraints.repository.IRoleRepository;
import domain.constraints.repository.IUserRepository;

import domain.usecase.Usecase;

import domain.entity.User;
import domain.entity.Role;

import domain.constraints.RoleObject;

public class UsecaseWelcome extends Usecase {

    public static final int RESULT_USER_RETRIEVED_SUCCESSFULLY = 1;
    public static final int RESULT_USER_NOT_FOUND = 2;

    // Factory
    private IUserRepository userRepository;
    private IRoleRepository roleRepository;

    // Input data
    private Integer refUserId = null;

    public void setRefUserId(Integer refUserId) {
        if (refUserId == null) {
            throw new IllegalArgumentException("refUserId cannot be null");
        }

        this.refUserId = refUserId;
    }

    // Output data
    private String username = null;
    private Role[] roles = null;

    public String getUsername() {
        return username;
    }

    public RoleObject[] getRoles() {
        return roles;
    }

    // Constructor

    public UsecaseWelcome(IUserRepository userRepository, IRoleRepository roleRepository) throws Exception {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    // Business Logic
    public int execute() throws Exception {

        if (refUserId == null) {
            throw new IllegalStateException("refUserId not provided");
        }
        else {

            User user = new User(refUserId);

            if (userRepository.findUser(user)) {
                username = user.getUsername();
                roles = roleRepository.getRolesByUser(user);
                return RESULT_USER_RETRIEVED_SUCCESSFULLY;
            }

            return RESULT_USER_NOT_FOUND;
        }
    }
}
