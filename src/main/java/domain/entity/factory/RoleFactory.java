package domain.entity.factory;

import adapter.repository.RoleRepository;

import java.util.Vector;
import domain.entity.Role;

public class RoleFactory {

    private static final int ROLE_ID = 0;
    private static final int ROLE_NAME = 1;
    private static final int ROLE_PAGE = 2;

    private RoleRepository roleRepository;

    public RoleFactory(RoleRepository model) {
        this.roleRepository = model;
    }

    /**
     * Create array of Role objects from an array of their respective ids
     * 
     * @param rids
     * @return
     * @throws Exception
     */
    public Role[] createByIds(Integer rids[]) throws Exception {

        Vector<Object[]> roles = roleRepository.getRolesByIds(rids);

        if (roles != null && roles.size() > 0) {
            Role[] roleObjects = new Role[roles.size()];
            for (int i = 0; i < roleObjects.length; i++) {
                Integer rid = (Integer) roles.get(i)[ROLE_ID];
                String name = (String) roles.get(i)[ROLE_NAME];
                String page = (String) roles.get(i)[ROLE_PAGE];
                roleObjects[i] = new Role(rid, name, page);
            }
            return roleObjects;
        }

        return null;
    }
}
