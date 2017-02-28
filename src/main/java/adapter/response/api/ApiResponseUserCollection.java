package adapter.response.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import domain.entity.Role;
import domain.entity.User;

@XmlRootElement(name = "ApiResponse")
public class ApiResponseUserCollection extends ApiResponse {

    public String getXml() throws Exception {

        return ApiResponse.getXml(this);
    }

    @XmlElementWrapper(name = "users")
    @XmlElement(name = "user")
    private User[] users;

    @XmlElementWrapper(name = "roles")
    @XmlElement(name = "role")
    private Role[] roles;

    public ApiResponseUserCollection() {
        users = new User[0];
        roles = new Role[0];
    }

    public ApiResponseUserCollection(User[] users, Role[] roles) {
        this.setUsers(users);
        this.setRoles(roles);
    }

    @XmlTransient
    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        if (users != null) {
            this.users = users;
        } else {
            users = new User[0];
        }
    }

    @XmlTransient
    public Role[] getRoles() {
        return roles;
    }

    public void setRoles(Role[] roles) {
        if (roles != null) {
            this.roles = roles;
        } else {
            roles = new Role[0];
        }
    }
}
