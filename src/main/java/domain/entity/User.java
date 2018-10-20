package domain.entity;

import com.google.common.base.Strings;
import domain.Helper;
import domain.contract.entity.UserObject;

import java.util.Objects;

public class User implements UserObject {

    protected Integer id;
    protected String username;
    protected String password;
    protected Integer[] roles;

    public User(){
        this.id = null;
        this.username = "";
        this.roles = new Integer[]{};
        password = "";
    }

    public User(User user){
        copyFrom(user);
    }

    public User(UserObject user){
        copyFrom(user);
        password = (!Strings.isNullOrEmpty(user.getPassword())) ? Helper.SHA1(user.getPassword()) : "";
    }

    public void copyFrom(UserObject user){
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
        this();
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

    public User(int id, String username) {
        this();
        this.id = id;
        this.username = username;
    }

    public User(int id) {
        this();
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Integer[] getRoleIds() {
        return roles;
    }

    public String getPassword() {
        return password;
    }

    public boolean containsValidData(){
        return (!Strings.isNullOrEmpty(getUsername()) &&
                !Strings.isNullOrEmpty(getPassword()) &&
                getRoleIds() != null && getRoleIds().length > 0);
    }

    public boolean sameIdAs(User user){

        return id.equals(user.getId());
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
        if(roles != null) {
            this.roles = roles;
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {

        if(o == null){
            return false;
        }

        if (o.getClass() != this.getClass()) {
            return false;
        }

        UserObject user = (UserObject) o;

        if (!Objects.equals(user.getId(), id)  ||
                !Objects.equals(user.getUsername(), username) ||
                !Objects.equals(user.getPassword(), password)) {

            return false;
        }

        if(user.getRoleIds() == null && roles != null ||
                user.getRoleIds() != null && roles == null ){

            return false;
        }

        if(user.getRoleIds() != null && roles != null) {
            if(user.getRoleIds().length != roles.length){
                return false;
            }
            for (int i = 0; i < roles.length; i++) {
                if (!Objects.equals(user.getRoleIds()[i], roles[i])) {
                    return false;
                }
            }
        }

        return true;
    }
}