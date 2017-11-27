package adapter.repository;

import adapter.repository.nosql.UserRepositoryMongo;
import adapter.repository.relational.UserRepositoryRelational;
import domain.constraints.repository.IUserRepository;
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

    public User[] getAllUsers() throws Exception {
        return repo.getAllUsers();
    }

    public User getUser(Integer uid) throws Exception {
        return repo.getUser(uid);
    }

    public User getUser(String username) throws Exception {
        return repo.getUser(username);
    }

    public User getUser(String username, String password) throws Exception {
        return repo.getUser(username, password);
    }

    public Integer insertUser(User user) throws Exception {
        Integer uid = repo.insertUser(user);

        if(uid != null){
            user.setId(uid);
        }

        return uid;
    }

    public boolean updateUser(User user) throws Exception {
        return repo.updateUser(user);
    }

    public boolean deleteUser(Integer uid) throws Exception {
        return repo.deleteUser(uid);
    }
}
