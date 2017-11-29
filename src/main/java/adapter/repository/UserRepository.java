package adapter.repository;

import domain.constraints.repository.IUserRepository;

import adapter.repository.nosql.UserRepositoryMongo;
import adapter.repository.relational.UserRepositoryRelational;

import domain.entity.User;

public class UserRepository extends Repository implements IUserRepository {

    protected Class getRelationalClass(){
        return UserRepositoryRelational.class;
    }

    protected Class getMongoClass(){
        return UserRepositoryMongo.class;
    }

    private IUserRepository repo;

    public UserRepository() throws Exception {
        repo = (IUserRepository) createRepository(this);
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
