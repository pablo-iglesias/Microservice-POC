package domain.contract.entity;

/**
 * Any object representing a user inside or outside of the domian package must implement this
 */
public interface UserObject {

    Integer getId();
    String getUsername();
    Integer[] getRoleIds();
    String getPassword();
}
