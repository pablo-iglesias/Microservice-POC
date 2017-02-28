package domain.entity;

import javax.xml.bind.annotation.XmlAttribute;

public class Role {

    @XmlAttribute(name = "id")
    private int id;

    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name = "page")
    private String page;

    public Role(int id, String name, String page) {
        this.id = id;
        this.name = name;
        this.page = page;
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

        if (o.getClass() != this.getClass()) {
            return false;
        }

        Role role = (Role) o;

        if (role.getId() != id || role.getName() != name || role.getPage() != page) {

            return false;
        }

        return true;
    }
}
