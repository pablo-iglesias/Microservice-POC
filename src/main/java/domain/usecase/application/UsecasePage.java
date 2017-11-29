package domain.usecase.application;

import domain.constraints.repository.IRoleRepository;
import domain.constraints.repository.IUserRepository;

import domain.service.UserService;
import domain.usecase.Usecase;

public class UsecasePage extends Usecase {

    public static final int RESULT_PAGE_RETRIEVED_SUCCESSFULLY = 1;
    public static final int RESULT_PAGE_NOT_ALLOWED = 2;

    private IUserRepository userRepository;
    private UserService service;

    // Input data
    private Integer refUserId = 0;
    private Integer page = 0;

    public void setRefUserId(Integer refUserId) {
        if (refUserId == null) {
            throw new IllegalArgumentException("refUserId cannot be null");
        }

        this.refUserId = refUserId;
    }

    public void setPage(Integer page) {
        if (page == null) {
            throw new IllegalArgumentException("page cannot be null");
        }

        this.page = page;
    }

    // Output data
    private String username = null;

    public String getUsername() {
        return username;
    }

    // Constructor
    public UsecasePage(IUserRepository userRepository, IRoleRepository roleRepository) throws Exception {
        this.userRepository = userRepository;
        service = new UserService(userRepository, roleRepository);
    }

    // Business Logic
    public int execute() throws Exception {

        if (refUserId == null) {
            throw new IllegalStateException("refUserId not provided");
        }
        else if (page == null) {
            throw new IllegalStateException("page not provided");
        }
        else if (service.isUserAllowedIntoPage(refUserId, page)) {
            username = service.getUserNameByUserId(refUserId);
            return RESULT_PAGE_RETRIEVED_SUCCESSFULLY;
        }
        else{
            return RESULT_PAGE_NOT_ALLOWED;
        }
    }
}
