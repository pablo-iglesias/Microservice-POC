package domain.entity;

import com.google.common.base.Strings;
import domain.Helper;
import domain.constraints.UserObject;

import java.util.Objects;

public class User extends UserObject {

    public User(UserObject user){
        id = user.getId();
        username = user.getUsername();
        password = (!Strings.isNullOrEmpty(user.getPassword())) ? Helper.SHA1(user.getPassword()) : "";
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
        this.password = (!Strings.isNullOrEmpty(password)) ? Helper.SHA1(password) : "";
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

    public User(int id) {
        this.id = id;
        this.username = "";
        this.roles = new Integer[]{};
        password = "";
    }

    public User() {
        this.id = null;
        this.username = "";
        this.roles = new Integer[]{};
        password = "";
    }

    public boolean containsValidData(){
        return (!Strings.isNullOrEmpty(getUsername()) &&
                !Strings.isNullOrEmpty(getPassword()) &&
                getRoleIds() != null && getRoleIds().length > 0);
    }

    public boolean sameIdAs(User user){

        return Objects.equals(id, user.getId());
    }

    public User setId(Integer id){
        this.id = id;
        return this;
    }

    public User setUsername(String username){
        this.username = username;
        return this;
    }

    public User setPassword(String password){
        this.password = (!Strings.isNullOrEmpty(password)) ? Helper.SHA1(password) : "";
        return this;
    }

    public User setRoles(Integer[] roles){
        this.roles = roles;
        return this;
    }
}
