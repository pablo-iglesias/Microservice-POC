package domain.usecase.api;

import domain.constraints.RoleObject;
import domain.constraints.UserObject;

import domain.constraints.repository.IRoleRepository;
import domain.constraints.repository.IUserRepository;

import domain.service.UserService;
import domain.usecase.Usecase;

import domain.entity.Role;
import domain.entity.User;

public class UsecaseGetOneUser extends Usecase {

    public static final int RESULT_USER_RETRIEVED_SUCCESSFULLY = 1;
    public static final int RESULT_USER_NOT_FOUND = 2;

    private UserService service;
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
    private User user = null;
    private Role[] roles = null;

    public UserObject getUser() {
        return user;
    }

    public RoleObject[] getRoles() {
        return roles;
    }

    // Constructor
    public UsecaseGetOneUser(IUserRepository userRepository, IRoleRepository roleRepository) throws Exception {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        service = new UserService(userRepository, roleRepository);
    }

    // Business Logic
    public int execute() throws Exception {

        if (refUserId == null) {
            throw new IllegalStateException("refUserId not provided");
        }
        else{
            user = new User(refUserId);

            if (userRepository.findUser(user)) {
                return RESULT_USER_NOT_FOUND;
            }
            else {
                roles = roleRepository.getRolesByUser(user);
                return RESULT_USER_RETRIEVED_SUCCESSFULLY;
            }
        }
    }
}