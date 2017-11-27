package adapter.response.model;

import domain.constraints.UserObject;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class UserModel {

    @XmlAttribute(name="id")
    protected int id;

    @XmlAttribute(name="name")
    protected String username;

    @XmlElement(name="role_id")
    protected Integer[] roles;

    public UserModel(UserObject user){
        id = user.getId();
        username = user.getUsername();
        roles = user.getRoleIds();
    }
}
