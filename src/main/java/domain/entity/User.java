package domain.entity;

import domain.model.UserModel;

public class User extends UserModel{

    public User(UserModel m){
        id = m.getId();
        username = m.getUsername();
        password = m.getPassword();
        roles = m.getRoles();
    }

    public User(int id, String username, String password, Integer[] roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public User(int id, String username, Integer[] roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
        password = "";
    }

    public boolean equals(Object o) {

        if (o.getClass() != this.getClass()) {
            return false;
        }

        User user = (User) o;

        if (    user.getId() != id || 
                !user.getUsername().equals(username) ||
                !user.getPassword().equals(password) ||
                user.getRoles().length != roles.length) {

            return false;
        }

        for (int i = 0; i < roles.length; i++) {
            if (!user.getRoles()[i].equals(roles[i])) {
                return false;
            }
        }

        return true;
    }
}
