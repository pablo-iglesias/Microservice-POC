package adapter.response.api.json;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import domain.entity.Role;
import domain.entity.User;

@XmlRootElement
public class ApiResponseUserResource extends ApiResponse{
	
	public String getXml() throws Exception{
		
        return ApiResponse.getXml(this);
	}

	private User user;
	private Role[] roles;
	
	public ApiResponseUserResource(){
		user = null;
		roles = null;
	}
	
	public ApiResponseUserResource(User user, Role[] roles){
		this.setUser(user);
		this.setRoles(roles);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role[] getRoles() {
		return roles;
	}

	public void setRoles(Role[] roles) {
		this.roles = roles;
	}
	
	
}
