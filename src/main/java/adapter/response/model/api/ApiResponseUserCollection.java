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
public class ApiResponseUserCollection extends ApiResponse {

    public String getXml() throws Exception {

        return getXml(this);
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

    public ApiResponseUserCollection(UserObject[] users, RoleObject[] roles) {

        this.users = new UserModel[users.length];
        this.roles = new RoleModel[roles.length];

        for(int i = 0; i < users.length; i++){
            this.users[i] = new UserModel(users[i]);
        }

        for(int i = 0; i < roles.length; i++){
            this.roles[i] = new RoleModel(roles[i]);
        }
    }

    @XmlTransient
    public UserModel[] getUsers() {
        return users;
    }

    @XmlTransient
    public RoleModel[] getRoles() {
        return roles;
    }
}
