package adapter.response.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import domain.model.RoleModel;
import domain.model.UserModel;

@XmlRootElement(name = "ApiResponse")
public class ApiResponseUserCollection extends ApiResponse {

    public String getXml() throws Exception {

        return ApiResponse.getXml(this);
    }

    @XmlElementWrapper(name = "users")
    @XmlElement(name = "user")
    private UserModel[] users;

    @XmlElementWrapper(name = "roles")
    @XmlElement(name = "role")
    private RoleModel[] roles;

    public ApiResponseUserCollection() {
        users = new UserModel[0];
        roles = new RoleModel[0];
    }

    public ApiResponseUserCollection(UserModel[] users, RoleModel[] roles) {
        this.setUsers(users);
        this.setRoles(roles);
    }

    @XmlTransient
    public UserModel[] getUsers() {
        return users;
    }

    public void setUsers(UserModel[] users) {
        if (users != null) {
            this.users = users;
        }
    }

    @XmlTransient
    public RoleModel[] getRoles() {
        return roles;
    }

    public void setRoles(RoleModel[] roles) {
        if (roles != null) {
            this.roles = roles;
        }
    }
}
