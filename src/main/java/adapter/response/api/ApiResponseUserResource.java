package adapter.response.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import domain.model.RoleModel;
import domain.model.UserModel;

@XmlRootElement(name = "ApiResponse")
public class ApiResponseUserResource extends ApiResponse {

    public String getXml() throws Exception {

        return ApiResponse.getXml(this);
    }

    @XmlElement(name = "user")
    private UserModel user;

    @XmlElementWrapper(name = "roles")
    @XmlElement(name = "role")
    private RoleModel[] roles;

    public ApiResponseUserResource() {
        user = null;
        roles = null;
    }

    public ApiResponseUserResource(UserModel user, RoleModel[] roles) {
        this.setUser(user);
        this.setRoles(roles);
    }

    @XmlTransient
    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    @XmlTransient
    public RoleModel[] getRoles() {
        return roles;
    }

    public void setRoles(RoleModel[] roles) {
        this.roles = roles;
    }

}
