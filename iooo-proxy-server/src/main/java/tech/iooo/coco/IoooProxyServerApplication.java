package tech.iooo.coco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import tech.iooo.coco.entity.ServerProperties;

/**
 * @author Ivan97
 */
@SpringBootApplication
@EnableConfigurationProperties(ServerProperties.class)
public class IoooProxyServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(IoooProxyServerApplication.class, args);
  }
}
