package adapter.response.model.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import adapter.response.model.RoleModel;
import adapter.response.model.UserModel;
import domain.constraints.RoleObject;
import domain.constraints.UserObject;

@XmlRootElement(name = "ApiResponse")
public class ApiResponseUserResource extends ApiResponse {

    public String getXml() throws Exception {

        return getXml(this);
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

    public ApiResponseUserResource(UserObject user, RoleObject[] roles) {

        this.user = new UserModel(user);
        this.roles = new RoleModel[roles.length];

        for(int i = 0; i < roles.length; i++){
            this.roles[i] = new RoleModel(roles[i]);
        }
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
