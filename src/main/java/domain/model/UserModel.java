package domain.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class UserModel {

    @XmlAttribute(name = "id")
    protected int id;

    @XmlAttribute(name = "name")
    protected String username = null;

    protected String password = null;

    @XmlElement(name = "role_id")
    protected Integer[] roles = null;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Integer[] getRoles() {
        return roles;
    }

    public String getPassword() {
        return password;
    }
}
