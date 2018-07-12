package tech.iooo.coco.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.socksx.v5.Socks5CommandRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5InitialRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5ServerEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import tech.iooo.coco.commons.ProxyIdleHandler;
import tech.iooo.coco.commons.ss5.Socks5CommandRequestHandler;
import tech.iooo.coco.commons.ss5.Socks5InitialRequestHandler;
import tech.iooo.coco.entity.ServerProperties;

/**
 * Created on 2018/7/11 下午8:37
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-proxy-moomba">Ivan97</a>
 */
@Component
public class IoooSocks5Server implements SmartLifecycle {

  private static final Logger logger = LoggerFactory.getLogger(IoooSocks5Server.class);
  private boolean running;

  @Autowired
  private ServerProperties serverProperties;

  @Override
  public boolean isAutoStartup() {
    return true;
  }

  @Override
  public void stop(Runnable callback) {
    this.running = false;
  }

  @Override
  public void start() {

    EventLoopGroup boss = new NioEventLoopGroup(2);
    EventLoopGroup worker = new NioEventLoopGroup();
    EventLoopGroup bossGroup = new NioEventLoopGroup();

    try {
      ServerBootstrap bootstrap = new ServerBootstrap();
      bootstrap.group(boss, worker)
          .channel(NioServerSocketChannel.class)
          .option(ChannelOption.SO_BACKLOG, 1024)
          .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
          .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
              //channel超时处理
              ch.pipeline().addLast(new IdleStateHandler(3, 30, 0));
              ch.pipeline().addLast(new ProxyIdleHandler());
              //netty日志
              ch.pipeline().addLast(new LoggingHandler());
              //Socks5MessageByteBuf
              ch.pipeline().addLast(Socks5ServerEncoder.DEFAULT);
              //sock5 init
              ch.pipeline().addLast(new Socks5InitialRequestDecoder());
              //sock5 init
              ch.pipeline().addLast(new Socks5InitialRequestHandler());
              //socks connection
              ch.pipeline().addLast(new Socks5CommandRequestDecoder());
              //Socks connection
              ch.pipeline().addLast(new Socks5CommandRequestHandler(bossGroup));
            }
          });

      ChannelFuture future = bootstrap.bind(serverProperties.getPort()).sync();
      
      logger.info("Listening on port : [{}]", serverProperties.getPort());
      
      future.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      boss.shutdownGracefully();
      worker.shutdownGracefully();
    }

    this.running = true;
  }

  @Override
  public void stop() {
    this.running = false;
  }

  @Override
  public boolean isRunning() {
    return this.running;
  }

  @Override
  public int getPhase() {
    return Integer.MAX_VALUE;
  }
}
