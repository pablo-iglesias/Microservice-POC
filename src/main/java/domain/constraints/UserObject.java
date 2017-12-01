package domain.constraints;

import java.util.Objects;

/**
 * User value object, entity will inherit this class
 */
public class UserObject {

    protected Integer id;
    protected String username = null;
    protected String password = null;
    protected Integer[] roles = null;

    public UserObject(){
        super();
    }

    public UserObject(UserObject user){
        copyFrom(user);
    }

    public void copyFrom(UserObject user){
        id = user.getId();
        username = user.getUsername();
        password = user.getPassword();
        roles = user.getRoleIds();
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
