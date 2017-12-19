package adapter.repository;

import adapter.repository.factory.RepositoryFactory;
import domain.constraints.repository.IRoleRepository;

import domain.entity.Role;
import domain.entity.User;

public class RoleRepository implements IRoleRepository {

    private IRoleRepository repo;

    public RoleRepository() throws Exception {
        repo = (IRoleRepository) RepositoryFactory.createRepository(this.getClass());
    }

    public Role getRole(Integer rid) throws Exception {
        return repo.getRole(rid);
    }

    public Role[] getRolesByUser(User user) throws Exception {
        return repo.getRolesByUser(user);
    }
}
