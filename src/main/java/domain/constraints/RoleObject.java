package domain.constraints;

import java.util.Objects;

/**
 * Role value object, entity will inherit this class
 */
public class RoleObject {

    protected int id;
    protected String name;
    protected String page;

    public RoleObject(){
        super();
    }

    public RoleObject(RoleObject role){
        id = role.getId();
        name = role.getName();
        page = role.getPage();
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
