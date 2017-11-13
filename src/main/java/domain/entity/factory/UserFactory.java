package domain.entity.factory;

import java.util.Vector;

import adapter.repository.RoleRepository;
import adapter.repository.UserRepository;

import domain.Helper;
import domain.entity.User;

public class UserFactory {

    private static final int USER_ID = 0;
    private static final int USER_NAME = 1;

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public UserFactory(UserRepository userModel, RoleRepository roleModel) {
        this.userRepository = userModel;
        this.roleRepository = roleModel;
    }

    /**
     * Create an array of User objects representing all the users in existance
     * 
     * @return
     * @throws Exception
     */
    public User[] create() throws Exception {

        Vector<Object[]> records = userRepository.getUsers();

        if (records != null) {
            User[] users = new User[records.size()];
            for (int i = 0; i < users.length; i++) {
                Integer uid = (Integer) records.get(i)[USER_ID];
                String name = (String) records.get(i)[USER_NAME];
                users[i] = new User(uid, name, roleRepository.getRoleIdsByUserId(uid));
            }
            return users;
        }

        return null;
    }

    /**
     * Create a User object from the user id
     * 
     * @param uid
     * @return
     * @throws Exception
     */
    public User create(int uid) throws Exception {

        String name = userRepository.selectUsernameByUserId(uid);
        if (name != null) {
            return new User(uid, name, roleRepository.getRoleIdsByUserId(uid));
        }
        return null;
    }

    /**
     * Create user object from username and password
     * 
     * @param name
     * @param password
     * @return
     * @throws Exception
     */
    public User create(String name, String password) throws Exception {

        if (name != null && password != null) {
            String hash = Helper.SHA1(password);
            Integer uid = userRepository.selectUserIdByUseranameAndPassword(name, hash);
            if (uid != null) {
                return new User(uid, name, roleRepository.getRoleIdsByUserId(uid));
            }
        }
        return null;
    }
}
