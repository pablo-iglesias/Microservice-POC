package adapter.repository;

import domain.constraints.repository.IRoleRepository;

import adapter.repository.nosql.RoleRepositoryMongo;
import adapter.repository.relational.RoleRepositoryRelational;

import domain.entity.Role;
import domain.entity.User;

public class RoleRepository extends Repository implements IRoleRepository {

    protected Class getRelationalClass(){
        return RoleRepositoryRelational.class;
    }

    protected Class getMongoClass(){
        return RoleRepositoryMongo.class;
    }

    private IRoleRepository repo;

    public RoleRepository() throws Exception {
        repo = (IRoleRepository) createRepository(this);
    }

    public Role getRole(Integer rid) throws Exception {
        return repo.getRole(rid);
    }

    public Role[] getRolesByUser(User user) throws Exception {
        return repo.getRolesByUser(user);
    }
}
