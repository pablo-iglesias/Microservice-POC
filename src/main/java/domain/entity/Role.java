package domain.entity;

import domain.constraints.RoleObject;

import java.util.Objects;

public class Role extends RoleObject {

    private static String ADMIN_ROLE_NAME = "ADMIN";

    public Role(RoleObject role){
        super(role);
    }

    public Role(int id) {
        this.id = id;
    }

    public Role(int id, String name, String page) {
        this.id = id;
        this.name = name;
        this.page = page;
    }

    public boolean isAdminRole(){
        return (Objects.equals(name, ADMIN_ROLE_NAME));
    }
}
