package domain.usecase.application;

import domain.usecase.Usecase;

import domain.entity.Role;
import domain.entity.User;

import domain.model.RoleModel;
import domain.model.UserModel;

import domain.factory.RoleFactory;
import domain.factory.UserFactory;

public class UsecasePage extends Usecase {

    public static final int RESULT_PAGE_RETRIEVED_SUCCESSFULLY = 1;
    public static final int RESULT_PAGE_NOT_ALLOWED = 2;
    public static final int RESULT_PAGE_NOT_FOUND = 3;

    // Factory
    private UserFactory userFactory;
    private RoleFactory roleFactory;

    // Input data
    private Integer refUserId = 0;
    private Integer page = 0;

    // Output data
    private String username = null;

    // Getter & Setter
    public int getRefUserId() {
        return refUserId;
    }

    public void setRefUserId(Integer refUserId) {
        if (refUserId == null) {
            throw new IllegalArgumentException("refUserId cannot be null");
        }

        this.refUserId = refUserId;
    }

    public Integer getPage() {
        return page;
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

    // Constructor
    public UsecasePage(UserModel userModel, RoleModel roleModel) throws Exception {
        userFactory = new UserFactory(userModel, roleModel);
        roleFactory = new RoleFactory(roleModel);
    }

    public UsecasePage(UserFactory userFactory, RoleFactory roleFactory) {
        this.userFactory = userFactory;
        this.roleFactory = roleFactory;
    }

    // Business Logic
    public int execute() throws Exception {

        if (refUserId == null) {
            throw new IllegalStateException("refUserId not provided");
        }

        if (page == null) {
            throw new IllegalStateException("page not provided");
        }

        User user = userFactory.create(refUserId);

        if (user != null) {
            username = user.getUsername();

            Role[] roles = roleFactory.createByIds(user.getRoles());

            boolean allowed = false;
            for (Role role : roles) {
                if (role.getPage() != null && role.getPage().matches("page_" + page)) {
                    allowed = true;
                }
            }

            if(allowed) {
                return RESULT_PAGE_RETRIEVED_SUCCESSFULLY;
            }
            else{
                return RESULT_PAGE_NOT_ALLOWED;
            }
        }

        return RESULT_PAGE_NOT_FOUND;
    }
}
