package adapter.response.model.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import adapter.response.model.RoleModel;
import adapter.response.model.UserModel;
import domain.contract.entity.RoleObject;
import domain.contract.entity.UserObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XmlRootElement(name = "ApiResponse")
public class ApiResponseUserResource extends ApiResponse {

    @XmlElement(name = "user")
    private UserModel user = null;

    @XmlElementWrapper(name = "roles")
    @XmlElement(name = "role")
    private List<RoleModel> roles = new ArrayList<>();

    public ApiResponseUserResource(){

    }

    public ApiResponseUserResource(UserObject user, RoleObject[] roles) {

        if (user != null) {
            this.user = new UserModel(user);
        }

        if (roles != null && roles.length > 0 ) {
            Arrays.stream(roles).forEach((role) -> this.roles.add(new RoleModel(role)));
        }
    }
}
