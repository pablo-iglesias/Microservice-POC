package domain.entity;

import domain.contract.entity.RoleObject;

import java.util.Objects;

public class Role implements RoleObject {

    protected Integer id;
    protected String name;
    protected String page;

    private static String ADMIN_ROLE_NAME = "ADMIN";

    public Role(int id, String name, String page) {
        this.id = id;
        this.name = name;
        this.page = page;
    }

    public boolean isAdminRole(){
        return (name.equals(ADMIN_ROLE_NAME));
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPage() {
        return page;
    }

    @Override
    public boolean equals(Object o) {

        if(o == null){
            return false;
        }

        if (o.getClass() != this.getClass()) {
            return false;
        }

        RoleObject role = (RoleObject) o;

        if (!Objects.equals(role.getId(), id) ||
                !Objects.equals(role.getName(), name) ||
                !Objects.equals(role.getPage(), page)) {

            return false;
        }

        return true;
    }
}
