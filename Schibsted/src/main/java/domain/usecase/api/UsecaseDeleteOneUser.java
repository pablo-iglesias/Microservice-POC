package domain.usecase.api;

import adapter.model.generic.RoleModel;
import adapter.model.generic.UserModel;

public class UsecaseDeleteOneUser {

	public static final int RESULT_USER_DELETED_SUCCESSFULLY = 1;
	public static final int RESULT_NOT_AUTHORISED = 2;
	public static final int RESULT_USER_DOES_NOT_EXIST = 3;
	public static final int RESULT_USER_NOT_DELETED = 4;
	public static final int RESULT_BAD_INPUT_DATA = 5;
	
	private UserModel userModel;
	private RoleModel roleModel;
		
	// Input data
	public Integer authUserId = null;
	public Integer refUserId = null;
	
	public UsecaseDeleteOneUser() throws Exception{
		userModel = new UserModel();
		roleModel = new RoleModel();
	}
	
	public UsecaseDeleteOneUser(UserModel userModel, RoleModel roleModel){
		this.userModel = userModel;
		this.roleModel = roleModel;
	}
	
	public int execute() throws Exception{
		
		if(authUserId != null && refUserId != null){
			if(userModel.selectUserIsAdminRole(authUserId)){
				if(userModel.selectUserExists(refUserId)){
					if(userModel.deleteUser(refUserId)){
						if(!roleModel.deleteUserHasRolesByUserId(refUserId)){
							return RESULT_USER_NOT_DELETED;
						}
					}
						
					return RESULT_USER_DELETED_SUCCESSFULLY;
					
				}
				else{
					return RESULT_USER_DOES_NOT_EXIST;
				}
			}
			else{
				return RESULT_NOT_AUTHORISED;
			}
		}
		else{
			return RESULT_BAD_INPUT_DATA;
		}
	}
}
