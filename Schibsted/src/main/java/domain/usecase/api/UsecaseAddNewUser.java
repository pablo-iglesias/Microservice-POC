package domain.usecase.api;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

import adapter.model.generic.RoleModel;
import adapter.model.generic.UserModel;

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
	
	// Output data
	public boolean allowed = false;
	
	public UsecaseAddNewUser() throws Exception{
		userModel = new UserModel();
		roleModel = new RoleModel();
	}
	
	public UsecaseAddNewUser(UserModel userModel, RoleModel roleModel){
		this.userModel = userModel;
		this.roleModel = roleModel;
	}
	
	public int execute() throws Exception{
		
		if(uid != null && user != null && user.getPassword() != null){
			allowed = userModel.selectUserIsAdminRole(uid.intValue());
			if(allowed){
				String hash = Hashing.sha1().hashString(user.getPassword(), Charsets.UTF_8 ).toString();
				if(userModel.selectUserIdByUseranameAndPassword(user.getUsername(), hash) != null){
					return RESULT_USER_ALREADY_EXISTS;
				}
				
				Integer newUserId = userModel.insertUser(user.getUsername(), hash);
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
