package domain.entity;

import domain.model.RoleModel;

public class Role extends RoleModel{

    public Role(RoleModel m){
        id = m.getId();
        name = m.getName();
        page = m.getPage();
    }

    public Role(int id, String name, String page) {
        this.id = id;
        this.name = name;
        this.page = page;
    }

    public boolean equals(Object o) {

        if (o.getClass() != this.getClass()) {
            return false;
        }

        Role role = (Role) o;

        if (role.getId() != id ||
            !role.getName().equals(name) ||
            !role.getPage().equals(page)) {

            return false;
        }

        return true;
    }
}
