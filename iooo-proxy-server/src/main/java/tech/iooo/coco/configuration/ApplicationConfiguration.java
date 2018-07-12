package tech.iooo.coco.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import tech.iooo.coco.properties.AuthProperties;
import tech.iooo.coco.properties.ServerProperties;

/**
 * Created on 2018/7/12 下午2:52
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-proxy-moomba">Ivan97</a>
 */
@Configuration
@EnableConfigurationProperties({ServerProperties.class, AuthProperties.class})
public class ApplicationConfiguration {

}
