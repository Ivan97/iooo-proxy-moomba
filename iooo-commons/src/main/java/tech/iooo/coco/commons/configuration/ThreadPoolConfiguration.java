package tech.iooo.coco.commons.configuration;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.iooo.coco.commons.entity.ThreadPoolProperties;

/**
 * Created on 2018/7/11 下午5:05
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-proxy-moomba">Ivan97</a>
 */
@Configuration
@EnableConfigurationProperties(ThreadPoolProperties.class)
public class ThreadPoolConfiguration {

  @Autowired
  private ThreadPoolProperties threadPoolProperties;

  @Bean
  @ConditionalOnMissingBean
  public ExecutorService executorService() {
    ThreadFactory threadFactory = new ThreadFactoryBuilder()
        .setNameFormat(threadPoolProperties.getExecutorService().getNameFormat())
        .setDaemon(threadPoolProperties.getExecutorService().getDaemon()).build();
    return new ThreadPoolExecutor(threadPoolProperties.getExecutorService().getCorePoolSize(),
        threadPoolProperties.getExecutorService().getMaximumPoolSize(),
        threadPoolProperties.getExecutorService().getKeepAliveTime(),
        threadPoolProperties.getExecutorService().getTimeUnit(),
        new LinkedBlockingDeque<>(threadPoolProperties.getExecutorService().getCapacity()), threadFactory,
        new AbortPolicy());
  }

  @Bean
  @ConditionalOnMissingBean
  public ScheduledExecutorService scheduledExecutorService() {
    return new ScheduledThreadPoolExecutor(threadPoolProperties.getScheduledExecutorService().getCorePoolSize(),
        new BasicThreadFactory.Builder()
            .namingPattern(threadPoolProperties.getScheduledExecutorService().getNamingPattern())
            .daemon(threadPoolProperties.getScheduledExecutorService().getDaemon()).build());
  }
}
