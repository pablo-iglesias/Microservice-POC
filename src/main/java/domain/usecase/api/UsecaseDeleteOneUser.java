package domain.usecase.api;

import domain.constraints.repository.IRoleRepository;
import domain.constraints.repository.IUserRepository;

import domain.entity.User;
import domain.service.UserService;
import domain.usecase.Usecase;

public class UsecaseDeleteOneUser extends Usecase{

    public static final int RESULT_USER_DELETED_SUCCESSFULLY = 1;
    public static final int RESULT_NOT_AUTHORISED = 2;
    public static final int RESULT_USER_DOES_NOT_EXIST = 3;
    public static final int RESULT_USER_NOT_DELETED = 4;

    private UserService service;
    private IUserRepository userRepository;
    private User user;

    // Input data
    private Integer authUserId = null;
    private Integer refUserId = null;

    public void setAuthUserId(Integer authUserId) {
        if (authUserId == null) {
            throw new IllegalArgumentException("authUserId cannot be null");
        }

        this.authUserId = authUserId;
    }

    public void setRefUserId(Integer refUserId) {
        if (refUserId == null) {
            throw new IllegalArgumentException("refUserId cannot be null");
        }

        this.refUserId = refUserId;
    }

    // Constructor
    public UsecaseDeleteOneUser(IUserRepository userRepository, IRoleRepository roleRepository) {
        this.userRepository = userRepository;
        service = new UserService(userRepository, roleRepository);
    }

    // Business Logic
    public int execute() throws Exception {

        if(authUserId == null){
            throw new IllegalStateException("authUserId not provided");
        }
        else if(refUserId == null){
            throw new IllegalStateException("refUserId not provided");
        }
        else if (!service.isUserAnAdmin(new User(authUserId))) {
            return RESULT_NOT_AUTHORISED;
        }
        else {
            User refUser = new User(refUserId);

            if (!userRepository.findUser(refUser)) {
                return RESULT_USER_DOES_NOT_EXIST;
            }
            else if (userRepository.deleteUser(refUser)) {
                return RESULT_USER_DELETED_SUCCESSFULLY;
            }
            else {
                return RESULT_USER_NOT_DELETED;
            }
        }
    }
}
