package adapter.model.generic;

import java.util.Vector;

import adapter.model.Model;
import adapter.model.RoleModelInterface;
import adapter.model.factory.ModelFactory;

/**
 * Generic Role model, abstracted from data source, suitable to be used from the domain scope
 * 
 * @author Peibol
 */
public class RoleModel extends Model implements RoleModelInterface {

	private ModelFactory factory;
	private RoleModelInterface model;
	
	public RoleModel() throws Exception{
		factory = new ModelFactory();
		model = (RoleModelInterface) factory.create("Role");
	}
	
	public Vector<Object[]> getRolesByIds(Integer rids[]) throws Exception{
		return model.getRolesByIds(rids);
	}
	
	public Integer[] getRoleIdsByUserId(Integer uid) throws Exception{
		return model.getRoleIdsByUserId(uid);
	}
	
	public boolean insertUserHasRoles(Integer uid, Integer[] rids) throws Exception{
		return model.insertUserHasRoles(uid, rids);
	}
	
	public boolean deleteUserHasRoles(Integer uid, Integer[] rids) throws Exception{
		return model.deleteUserHasRoles(uid, rids);
	}
	
	public boolean deleteUserHasRolesByUserId(Integer uid) throws Exception{
		return model.deleteUserHasRolesByUserId(uid);
	}
}
