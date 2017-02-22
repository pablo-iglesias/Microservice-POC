package adapter.model.generic;

import java.util.Vector;

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
	
	public Vector<Object[]> getRolesByIds(int rids[]) throws Exception{
		return model.getRolesByIds(rids);
	}
	
	public int[] getRoleIdsByUserId(int uid) throws Exception{
		return model.getRoleIdsByUserId(uid);
	}
}
