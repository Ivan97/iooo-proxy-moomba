package tech.iooo.coco.auth;

/**
 * @author Ivan97
 */
public interface PasswordAuth {

  boolean auth(String username, String password);

}
