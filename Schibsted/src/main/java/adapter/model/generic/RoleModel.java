package adapter.model.generic;

import java.sql.SQLException;

import adapter.model.Model;
import adapter.model.RoleModelInterface;
import adapter.model.factory.ModelFactory;

/**
 * Standard Role model, abstracted from data source
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
	
	public String getRoleNameByRoleId(int rid) throws SQLException{
		return model.getRoleNameByRoleId(rid);
	}
	
	public Integer[] getRolesIdsByUserId(int uid) throws Exception{
		return model.getRolesIdsByUserId(uid);
	}
}
