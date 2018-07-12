package tech.iooo.coco.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created on 2018/7/11 下午8:39
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-proxy-moomba">Ivan97</a>
 */
@Data
@ConfigurationProperties(prefix = "application.server")
public class ServerProperties {

  private Integer port = 1087;
}
