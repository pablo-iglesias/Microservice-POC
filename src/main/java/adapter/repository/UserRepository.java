package adapter.repository;

import adapter.repository.factory.RepositoryFactory;
import domain.constraints.repository.IUserRepository;

import domain.entity.User;

public class UserRepository implements IUserRepository {

    private IUserRepository repo;

    public UserRepository() throws Exception {
        repo = (IUserRepository) RepositoryFactory.createRepository(this.getClass());
    }

    /**
     * Get all users
     *
     * @return
     * @throws Exception
     */
    public User[] getAllUsers() throws Exception {
        return repo.getAllUsers();
    }

    /**
     * Find user
     *
     * @param user
     * @return
     * @throws Exception
     */
    public boolean findUser(User user) throws Exception {
        return repo.findUser(user);
    }

    /**
     *  Insert user
     *
     * @param user
     * @return
     * @throws Exception
     */
    public Integer insertUser(User user) throws Exception {

        if(!user.containsValidData()){
            return null;
        }

        Integer uid = repo.insertUser(user);

        if(uid != null){
            user.setId(uid);
        }

        return uid;
    }

    /**
     * Update user
     *
     * @param user
     * @return
     * @throws Exception
     */
    public boolean updateUser(User user) throws Exception {

        if(!user.containsValidData()){
            return false;
        }

        return repo.updateUser(user);
    }

    /**
     *  Delete user
     *
     * @param user
     * @return
     * @throws Exception
     */
    public boolean deleteUser(User user) throws Exception {

        if(user.getId() == null){
            return false;
        }

        return repo.deleteUser(user);
    }
}
