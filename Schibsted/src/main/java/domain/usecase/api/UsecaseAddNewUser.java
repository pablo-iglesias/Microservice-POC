package domain.usecase.api;

import adapter.model.generic.RoleModel;
import adapter.model.generic.UserModel;
import core.Helper;
import domain.entity.User;

public class UsecaseAddNewUser {

	public static final int RESULT_USER_CREATED_SUCCESSFULLY = 1;
	public static final int RESULT_NOT_AUTHORISED = 2;
	public static final int RESULT_USER_ALREADY_EXISTS = 3;
	public static final int RESULT_USER_NOT_CREATED = 4;
	public static final int RESULT_BAD_INPUT_DATA = 5;
	
	private UserModel userModel;
	private RoleModel roleModel;
	
	// Input data
	public Integer uid = null;
	public User user = null;
	
	public UsecaseAddNewUser() throws Exception{
		userModel = new UserModel();
		roleModel = new RoleModel();
	}
	
	public UsecaseAddNewUser(UserModel userModel, RoleModel roleModel){
		this.userModel = userModel;
		this.roleModel = roleModel;
	}
	
	public int execute() throws Exception{
		
		if(uid != null && user != null && user.getUsername() != null && user.getPassword() != null && user.getRoles() != null){
			boolean allowed = userModel.selectUserIsAdminRole(uid.intValue());
			if(allowed){
				
				if(userModel.selectUserExistsByUseraname(user.getUsername())){
					return RESULT_USER_ALREADY_EXISTS;
				}
				
				Integer newUserId = userModel.insertUser(user.getUsername(), Helper.SHA1(user.getPassword()));
				if(newUserId != null){
					if(roleModel.insertUserHasRoles(newUserId, user.getRoles())){
						return RESULT_USER_CREATED_SUCCESSFULLY;
					}
				}
				
				return RESULT_USER_NOT_CREATED;
			}
			else{
				return RESULT_NOT_AUTHORISED;
			}
		}
		
		return RESULT_BAD_INPUT_DATA;
	}
}
