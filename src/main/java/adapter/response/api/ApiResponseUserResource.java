package adapter.response.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import domain.entity.Role;
import domain.entity.User;

@XmlRootElement(name = "ApiResponse")
public class ApiResponseUserResource extends ApiResponse {

    public String getXml() throws Exception {

        return ApiResponse.getXml(this);
    }

    @XmlElement(name = "user")
    private User user;

    @XmlElementWrapper(name = "roles")
    @XmlElement(name = "role")
    private Role[] roles;

    public ApiResponseUserResource() {
        user = null;
        roles = null;
    }

    public ApiResponseUserResource(User user, Role[] roles) {
        this.setUser(user);
        this.setRoles(roles);
    }

    @XmlTransient
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @XmlTransient
    public Role[] getRoles() {
        return roles;
    }

    public void setRoles(Role[] roles) {
        this.roles = roles;
    }

}
