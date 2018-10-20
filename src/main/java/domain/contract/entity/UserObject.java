package domain.contract.entity;

/**
 * The entity role will implement this interface
 */
public interface UserObject {

    default void UserObject(){

    }

    Integer getId();
    String getUsername();
    Integer[] getRoleIds();
    String getPassword();
}
