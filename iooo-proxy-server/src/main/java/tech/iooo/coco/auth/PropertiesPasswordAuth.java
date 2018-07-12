package tech.iooo.coco.auth;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.iooo.coco.properties.AuthProperties;

/**
 * @author Ivan97
 */
@Service
public class PropertiesPasswordAuth implements PasswordAuth {

  @Autowired
  private AuthProperties authProperties;

  @Override
  public boolean auth(String username, String password) {
    return (Objects.equals(username, authProperties.getUsername())
        && Objects.equals(password, authProperties.getPassword()));
  }
}
