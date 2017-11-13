package domain.model;

import javax.xml.bind.annotation.XmlAttribute;

public class RoleModel {

    @XmlAttribute(name = "id")
    protected int id;

    @XmlAttribute(name = "name")
    protected String name;

    @XmlAttribute(name = "page")
    protected String page;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPage() {
        return page;
    }
}
