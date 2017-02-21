package domain.factory;

import java.util.Vector;

import adapter.model.generic.RoleModel;
import domain.entity.Role;

public class RoleFactory {

	private RoleModel model;
	
	public RoleFactory(RoleModel model) throws Exception{
		this.model = model;
	}
	
	public RoleFactory() throws Exception{
		model = new RoleModel();
	}
	
	public Role[] createByUser(int uid) throws Exception{
				
		Vector<Object[]> roles = model.getRolesByUserId(uid);
		
		if(roles != null){
			Role[] roleObjects = new Role[roles.size()];
			for(int i = 0; i < roleObjects.length; i++){
				Integer rid = (Integer)roles.get(i)[0];
				String name = (String)roles.get(i)[1];
				roleObjects[i] = new Role(rid, name);
			}
			return roleObjects;
		}
		
		return null;
	}
}
