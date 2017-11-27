package domain.service;

import domain.constraints.repository.IRoleRepository;
import domain.constraints.repository.IUserRepository;

import domain.entity.User;
import domain.entity.Role;

public class UserService {

    private IUserRepository userRepository;
    private IRoleRepository roleRepository;

    /**
     * Constructor
     *
     * @param userRepository
     * @param roleRepository
     */
    public UserService(IUserRepository userRepository, IRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * Create an array of User objects representing all the users in existance
     *
     * @return
     * @throws Exception
     */
    public User[] getAllUsers() throws Exception {

        User[] users = userRepository.getAllUsers();

        if (users != null) {
            return users;
        }

        return null;
    }

    /**
     * Does user exist
     *
     * @param uid
     * @return
     * @throws Exception
     */
    public boolean doesUserExist(Integer uid) throws Exception {
        return (userRepository.getUser(uid) != null);
    }

    /**
     * Does user exist
     *
     * @param username
     * @return
     * @throws Exception
     */
    public boolean doesUserExist(String username) throws Exception {
        return (userRepository.getUser(username) != null);
    }

    /**
     * Is user an admin
     *
     * @param uid
     * @return
     * @throws Exception
     */
    public boolean isUserAnAdmin(Integer uid) throws Exception {

        User user = userRepository.getUser(uid);
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
    public boolean createNewUser(User user) throws Exception {

        if(userRepository.insertUser(user) != null) {
            return true;
        }

        return false;
    }

    /**
     * Get username of the user by its id
     *
     * @param uid
     * @return
     * @throws Exception
     */
    public String getUserNameByUserId(Integer uid) throws Exception {

        User user =  userRepository.getUser(uid);
        if(user != null){
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
    public boolean isUserAllowedIntoPage(Integer uid, Integer page) throws Exception {

        User user = userRepository.getUser(uid);
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
