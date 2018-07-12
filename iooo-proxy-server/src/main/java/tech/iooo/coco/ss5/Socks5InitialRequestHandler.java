package tech.iooo.coco.ss5;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.socksx.SocksVersion;
import io.netty.handler.codec.socksx.v5.DefaultSocks5InitialRequest;
import io.netty.handler.codec.socksx.v5.DefaultSocks5InitialResponse;
import io.netty.handler.codec.socksx.v5.Socks5AuthMethod;
import io.netty.handler.codec.socksx.v5.Socks5InitialResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.iooo.coco.properties.ServerProperties;

/**
 * @author Ivan97
 */
@Service
@Sharable
public class Socks5InitialRequestHandler extends SimpleChannelInboundHandler<DefaultSocks5InitialRequest> {

  private static final Logger logger = LoggerFactory.getLogger(Socks5InitialRequestHandler.class);

  @Autowired
  private ServerProperties serverProperties;

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, DefaultSocks5InitialRequest msg) throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug("INITIAL OF THE SOCKS5 CONNECTION : " + msg);
    }
    if (msg.decoderResult().isFailure()) {
      if (logger.isDebugEnabled()) {
        logger.debug("THIS CONNECTION IS NOT SOCKS5");
      }
      ctx.fireChannelRead(msg);
    } else {
      if (msg.version().equals(SocksVersion.SOCKS5)) {
        if (serverProperties.isAuth()) {
          Socks5InitialResponse initialResponse = new DefaultSocks5InitialResponse(Socks5AuthMethod.PASSWORD);
          ctx.writeAndFlush(initialResponse);
        } else {
          Socks5InitialResponse initialResponse = new DefaultSocks5InitialResponse(Socks5AuthMethod.NO_AUTH);
          ctx.writeAndFlush(initialResponse);
        }
      }
    }
  }
}
