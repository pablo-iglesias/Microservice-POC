package domain.factory;

import adapter.model.generic.RoleModel;
import domain.entity.Role;

public class RoleFactory {

	RoleModel model;
	
	public RoleFactory() throws Exception{
		model = new RoleModel();
	}
	
	public Role create(int rid) throws Exception{
		
		String name = model.getRoleNameByRoleId(rid);
		if(name != null){
			return new Role(rid, name);
		}
		return null;
	}
	
	public Role[] createByUser(int uid) throws Exception{
		
		Integer[] rids = model.getRolesIdsByUserId(uid);
		
		if(rids != null){
			Role[] roles = new Role[rids.length];
			for(int i = 0; i < rids.length; i++){
				int rid = rids[i].intValue();
				String name = model.getRoleNameByRoleId(rid);
				roles[i] = new Role(rid, name);
			}
			return roles;
		}
		return null;
	}
}
