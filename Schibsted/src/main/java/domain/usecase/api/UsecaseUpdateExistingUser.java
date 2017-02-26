package domain.usecase.api;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

import adapter.model.generic.RoleModel;
import adapter.model.generic.UserModel;

import domain.entity.User;

public class UsecaseUpdateExistingUser {

	public static final int RESULT_USER_UPDATED_SUCCESSFULLY = 1;
	public static final int RESULT_NOT_AUTHORISED = 2;
	public static final int RESULT_USER_DOES_NOT_EXIST = 3;
	public static final int RESULT_USERNAME_ALREADY_TAKEN = 4;
	public static final int RESULT_USER_NOT_UPDATED = 5;
	public static final int RESULT_BAD_INPUT_DATA = 6;
	
	private UserModel userModel;
	private RoleModel roleModel;
	
	// Input data
	public Integer authUserId = null;
	public Integer refUserId = null;
	public User userData = null;
	
	public UsecaseUpdateExistingUser() throws Exception{
		userModel = new UserModel();
		roleModel = new RoleModel();
	}
	
	public UsecaseUpdateExistingUser(UserModel userModel, RoleModel roleModel){
		this.userModel = userModel;
		this.roleModel = roleModel;
	}
	
	public int execute() throws Exception{
		
		if(authUserId != null && refUserId != null && userData != null && userData.getUsername() != null && userData.getPassword() != null && userData.getRoles() != null){
			if(userModel.selectUserIsAdminRole(authUserId)){
				if(userModel.selectUserExists(refUserId)){
					Integer currentUsernameHolder = userModel.selectUserIdByUseraname(userData.getUsername());
					if(currentUsernameHolder == null || currentUsernameHolder.equals(refUserId)){
						
						String hash = Hashing.sha1().hashString(userData.getPassword(), Charsets.UTF_8 ).toString();				
						if(!userModel.updateUser(refUserId, userData.getUsername(), hash)){
							return RESULT_USER_NOT_UPDATED;
						}
							
						Integer[] currentRoles = roleModel.getRoleIdsByUserId(refUserId);
						Integer[] requiredRoles = userData.getRoles();
						
						List<Integer> currentRolesList = Arrays.asList(currentRoles);
						List<Integer> requiredRolesList = Arrays.asList(requiredRoles);
						
						Vector<Integer> addRoles = new Vector<Integer>();
						Vector<Integer> deleteRoles = new Vector<Integer>();
						
						for(int required : requiredRoles){
							if(!currentRolesList.contains(required)){
								addRoles.add(required);
							}
						}
						
						for(int current : currentRoles){
							if(!requiredRolesList.contains(current)){
								deleteRoles.add(current);
							}
						}
						
						if(addRoles.size() != 0){
							if(!roleModel.insertUserHasRoles(refUserId, (Integer[]) addRoles.toArray(new Integer[0]))){
								return RESULT_USER_NOT_UPDATED;
							}
						}
						
						if(deleteRoles.size() != 0){
							if(!roleModel.deleteUserHasRoles(refUserId, (Integer[]) deleteRoles.toArray(new Integer[0]))){
								return RESULT_USER_NOT_UPDATED;
							}
						}
						
						return RESULT_USER_UPDATED_SUCCESSFULLY;
					}
					else{
						return RESULT_USERNAME_ALREADY_TAKEN;
					}
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
