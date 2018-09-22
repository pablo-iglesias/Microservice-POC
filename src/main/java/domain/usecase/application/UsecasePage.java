package domain.usecase.application;

import domain.constraints.repository.IRoleRepository;
import domain.constraints.repository.IUserRepository;

import domain.service.UserService;
import domain.usecase.Usecase;

import javax.inject.Inject;

public class UsecasePage extends Usecase {

    public enum Result{
        PAGE_RETRIEVED_SUCCESSFULLY,
        PAGE_NOT_ALLOWED
    }

    private @Inject IUserRepository userRepository;
    private @Inject IRoleRepository roleRepository;
    private @Inject UserService service;
    private Integer refUserId = 0;
    private Integer page = 0;
    private String username = null;

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

    public String getUsername() {
        return username;
    }

    @Override
    public Result execute() throws Exception {

        if (refUserId == null) {
            throw new IllegalStateException("refUserId not provided");
        }

        if (page == null) {
            throw new IllegalStateException("page not provided");
        }

        if (service.isUserAllowedIntoPage(refUserId, page)) {
            username = service.getUserNameByUserId(refUserId);
            return Result.PAGE_RETRIEVED_SUCCESSFULLY;
        }

        return Result.PAGE_NOT_ALLOWED;
    }
}
