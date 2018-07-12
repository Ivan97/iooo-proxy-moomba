package tech.iooo.coco.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created on 2018/7/12 下午2:53
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-proxy-moomba">Ivan97</a>
 */
@Data
@ConfigurationProperties(prefix = "application.auth")
public class AuthProperties {

  private String username ;
  private String password;
}
