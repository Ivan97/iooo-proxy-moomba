package tech.iooo.coco.commons.entity;

import java.util.concurrent.TimeUnit;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created on 2018/7/11 下午5:08
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-proxy-moomba">Ivan97</a>
 */
@Data
@ConfigurationProperties(prefix = "application.threadPool")
public class ThreadPoolProperties {

  private ExecutorService executorService = new ThreadPoolProperties.ExecutorService();
  private ScheduledExecutorService scheduledExecutorService = new ThreadPoolProperties.ScheduledExecutorService();

  @Data
  public static class ExecutorService {

    private String nameFormat = "i-exec-pool-%d";
    private Boolean daemon = true;
    private Integer corePoolSize = 5;
    private Integer maximumPoolSize = 200;
    private Long keepAliveTime = 0L;
    private TimeUnit timeUnit = TimeUnit.MICROSECONDS;
    private Integer capacity = 1024;
  }

  @Data
  public static class ScheduledExecutorService {

    private String namingPattern = "i-scheduled-pool-%d";
    private Integer corePoolSize = 1;
    private Boolean daemon = true;
  }
}
