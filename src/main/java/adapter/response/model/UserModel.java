package adapter.response.model;

import domain.contract.entity.UserObject;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class UserModel implements UserObject {

    @XmlAttribute(name="id")
    private int id;

    @XmlAttribute(name="name")
    private String username;

    // The transient thing is for gson to ignore
    private transient String password;

    @XmlElement(name="role_id")
    private Integer[] roles;

    public UserModel(UserObject user){
        id = user.getId();
        username = user.getUsername();
        password = user.getPassword();
        roles = user.getRoleIds();
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Integer[] getRoleIds() {
        return roles;
    }

    public String getPassword() {
        return password;
    }
}
