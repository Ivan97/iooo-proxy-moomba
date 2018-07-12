package tech.iooo.coco.client;

import io.vertx.core.Vertx;
import io.vertx.core.net.ProxyOptions;
import io.vertx.core.net.ProxyType;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

/**
 * Created on 2018/7/12 下午3:59
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-proxy-moomba">Ivan97</a>
 */
@Service
public class IoooHttpClient implements CommandLineRunner {

  private static final Logger logger = LoggerFactory.getLogger(IoooHttpClient.class);

  @Override
  public void run(String... args) throws Exception {
    Vertx vertx = Vertx.vertx();
    WebClient client = WebClient.create(vertx, new WebClientOptions().setProxyOptions(new ProxyOptions()
        .setType(ProxyType.SOCKS5)
        .setHost("127.0.0.1")
        .setPort(1089)
        .setUsername("ivan97")
        .setPassword("test")
    ));

    vertx.setPeriodic(1000, i -> {
          logger.info("called...");
          client.get("https://www.baidu.com").send(result -> {
            if (result.succeeded()) {
              logger.info("statusCode:{}", result.result().statusCode());
            } else {
              logger.error("error", result.cause());
            }
          });
        }
    );
  }
}
