package adapter.response.model;

import domain.constraints.RoleObject;

import javax.xml.bind.annotation.XmlAttribute;

public class RoleModel {

    @XmlAttribute(name = "id")
    protected int id;

    @XmlAttribute(name = "name")
    protected String name;

    @XmlAttribute(name = "page")
    protected String page;

    public RoleModel(RoleObject role){
        id = role.getId();
        name = role.getName();
        page = role.getPage();
    }
}
