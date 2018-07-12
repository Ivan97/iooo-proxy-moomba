package tech.iooo.coco.commons.ss5;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.socksx.v5.DefaultSocks5InitialRequest;
import io.netty.handler.codec.socksx.v5.DefaultSocks5InitialResponse;
import io.netty.handler.codec.socksx.v5.Socks5AuthMethod;
import io.netty.handler.codec.socksx.v5.Socks5InitialResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ivan97
 */
public class Socks5InitialRequestHandler extends SimpleChannelInboundHandler<DefaultSocks5InitialRequest> {

  private static final Logger logger = LoggerFactory.getLogger(Socks5InitialRequestHandler.class);
  
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, DefaultSocks5InitialRequest msg) throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug("初始化ss5连接 : " + msg);
    }
    if (msg.decoderResult().isFailure()) {
      if (logger.isDebugEnabled()) {
        logger.debug("不是ss5协议");
      }
      ctx.fireChannelRead(msg);
    } else {
      Socks5InitialResponse initialResponse = new DefaultSocks5InitialResponse(Socks5AuthMethod.NO_AUTH);
      ctx.writeAndFlush(initialResponse);
    }
  }
}
