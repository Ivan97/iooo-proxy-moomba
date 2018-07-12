package tech.iooo.coco.test;

import io.vertx.core.Vertx;
import io.vertx.core.net.ProxyOptions;
import io.vertx.core.net.ProxyType;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import java.util.concurrent.CountDownLatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(JUnit4.class)
public class HttpProxyClient {

  private static final Logger logger = LoggerFactory.getLogger(HttpProxyClient.class);

  private int i = 10;
  private CountDownLatch latch = new CountDownLatch(i);

  @Test
  public void test() {
    for (int i = 0; i < this.i - 1; i++) {
      new Thread(new Runner()).start();
    }
    try {
      latch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  class Runner implements Runnable {

    @Override
    public void run() {
      Vertx vertx = Vertx.vertx();
      WebClient client = WebClient.create(vertx,
          new WebClientOptions().setProxyOptions(
              new ProxyOptions()
                  .setType(ProxyType.SOCKS5)
                  .setHost("127.0.0.1")
                  .setPort(1089)
                  .setUsername("ivan97")
                  .setPassword("test")));
      logger.info("called...");
      client.get("https://www.baidu.com").send(result -> {
        if (result.succeeded()) {
          logger.info("statusCode:{}", result.result().statusCode());
          latch.countDown();
        } else {
          logger.error("error", result.cause());
          latch.countDown();
        }
      });
    }
  }
}
