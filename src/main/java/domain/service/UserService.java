package domain.service;

import domain.contract.repository.IRoleRepository;
import domain.contract.repository.IUserRepository;

import domain.entity.User;
import domain.entity.Role;

import javax.inject.Inject;

public class UserService {

    @Inject private IUserRepository userRepository;
    @Inject private IRoleRepository roleRepository;

    /**
     * Is user an admin
     *
     * @param user
     * @return
     * @throws Exception
     */
    public boolean isUserAnAdmin(User user) {

        Role[] roles = roleRepository.getRolesByUser(user);

        for(Role role : roles){
            if(role.isAdminRole()){
                return true;
            }
        }

        return false;
    }

    /**
     * Create a new user
     *
     * @param user
     * @return
     * @throws Exception
     */
    public boolean createNewUser(User user) {

        return userRepository.insertUser(user) != null;
    }

    /**
     * Get username of the user by its id
     *
     * @param uid
     * @return
     * @throws Exception
     */
    public String getUserNameByUserId(Integer uid) {

        User user = new User(uid);
        if(userRepository.findUser(user)){
            return user.getUsername();
        }
        return null;
    }

    /**
     * Is user allowed into page
     *
     * @param uid
     * @param page
     * @return
     * @throws Exception
     */
    public boolean isUserAllowedIntoPage(Integer uid, Integer page) {

        User user = new User(uid);
        Role[] roles = roleRepository.getRolesByUser(user);

        for (Role role : roles) {
            if ( role.getPage() != null &&
                 role.getPage().matches("page_" + page)) {
                return true;
            }
        }

        return false;
    }
}
