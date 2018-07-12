package tech.iooo.coco.commons.ss5;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.socksx.v5.DefaultSocks5CommandRequest;
import io.netty.handler.codec.socksx.v5.DefaultSocks5CommandResponse;
import io.netty.handler.codec.socksx.v5.Socks5AddressType;
import io.netty.handler.codec.socksx.v5.Socks5CommandResponse;
import io.netty.handler.codec.socksx.v5.Socks5CommandStatus;
import io.netty.handler.codec.socksx.v5.Socks5CommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ivan97
 */
public class Socks5CommandRequestHandler extends SimpleChannelInboundHandler<DefaultSocks5CommandRequest> {

  private static final Logger logger = LoggerFactory.getLogger(Socks5CommandRequestHandler.class);
  private EventLoopGroup bossGroup;

  public Socks5CommandRequestHandler(EventLoopGroup bossGroup) {
    this.bossGroup = bossGroup;
  }

  @Override
  protected void channelRead0(final ChannelHandlerContext clientChannelContext, DefaultSocks5CommandRequest msg)
      throws Exception {
    String target = msg.dstAddr() + ":" + msg.dstPort();
    if (logger.isDebugEnabled()) {
      logger.debug("TARGET ADDRESS  : " + msg.type() + "," + target);

    }
    if (msg.type().equals(Socks5CommandType.CONNECT)) {
      if (logger.isTraceEnabled()) {
        logger.trace("PREPARING TO GET ACCESS TO THE TARGET ADDRESS:[{}]", target);
      }

      Bootstrap bootstrap = new Bootstrap();
      bootstrap.group(bossGroup)
          .channel(NioSocketChannel.class)
          .option(ChannelOption.TCP_NODELAY, true)
          .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
              //ch.pipeline().addLast(new LoggingHandler());//in out
              //将目标服务器信息转发给客户端
              ch.pipeline().addLast(new Dest2ClientHandler(clientChannelContext));
            }
          });
      if (logger.isTraceEnabled()) {
        logger.trace("CONNECTING TO THE TARGET ADDRESS:[{}]", target);
      }
      ChannelFuture future = bootstrap.connect(msg.dstAddr(), msg.dstPort());
      future.addListener((ChannelFutureListener) channelFuture -> {
        if (channelFuture.isSuccess()) {
          if (logger.isTraceEnabled()) {
            logger.trace("CONNECTED TO THE TARGET ADDRESS SUCCESSFULLY");
          }
          clientChannelContext.pipeline().addLast(new Client2DestHandler(channelFuture));
          Socks5CommandResponse commandResponse = new DefaultSocks5CommandResponse(
              Socks5CommandStatus.SUCCESS, Socks5AddressType.IPv4);
          clientChannelContext.writeAndFlush(commandResponse);
        } else {
          Socks5CommandResponse commandResponse = new DefaultSocks5CommandResponse(
              Socks5CommandStatus.FAILURE, Socks5AddressType.IPv4);
          clientChannelContext.writeAndFlush(commandResponse);
        }
      });
    } else {
      clientChannelContext.fireChannelRead(msg);
    }
  }

  /**
   * 将目标服务器信息转发给客户端
   *
   * @author huchengyi
   */
  private static class Dest2ClientHandler extends ChannelInboundHandlerAdapter {

    private ChannelHandlerContext clientChannelContext;

    public Dest2ClientHandler(ChannelHandlerContext clientChannelContext) {
      this.clientChannelContext = clientChannelContext;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx2, Object destMsg) throws Exception {
      if (logger.isTraceEnabled()) {
        logger.trace("SERVER=>PROXY=>CLIENT");
      }
      clientChannelContext.writeAndFlush(destMsg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx2) throws Exception {
      if (logger.isTraceEnabled()) {
        logger.trace("DISCONNECT FROM THE TARGET ADDRESS");
      }
      clientChannelContext.channel().close();
    }
  }

  /**
   * 将客户端的消息转发给目标服务器端
   *
   * @author huchengyi
   */
  private static class Client2DestHandler extends ChannelInboundHandlerAdapter {

    private ChannelFuture destChannelFuture;

    public Client2DestHandler(ChannelFuture destChannelFuture) {
      this.destChannelFuture = destChannelFuture;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      if (logger.isTraceEnabled()) {
        logger.trace("CLIENT=>PROXY=>SERVER");
      }
      destChannelFuture.channel().writeAndFlush(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
      if (logger.isTraceEnabled()) {
        logger.trace("CLIENT DISCONNECTED");
      }
      destChannelFuture.channel().close();
    }
  }
}
