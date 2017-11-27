package domain.entity;

import domain.Helper;
import domain.constraints.UserObject;

public class User extends UserObject {

    public User(UserObject user){
        id = user.getId();
        username = user.getUsername();
        password = (user.getPassword() != null && user.getPassword().length() > 0) ? Helper.SHA1(user.getPassword()) : "";
        roles = user.getRoleIds();
    }

    public User(User user){
        id = user.getId();
        username = user.getUsername();
        password = user.getPassword();
        roles = user.getRoleIds();
    }

    public User(int id, String username, String password, Integer[] roles) {
        this.id = id;
        this.username = username;
        this.password = (password != null && password.length() > 0) ? Helper.SHA1(password) : "";
        this.roles = roles;
    }

    public User(int id, String username, Integer[] roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
        password = "";
    }

    public User(int id, String username) {
        this.id = id;
        this.username = username;
        this.roles = new Integer[]{};
        password = "";
    }

    public void setId(Integer id){
        this.id = id;
    }
}
