package tech.iooo.coco.commons.component;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.stereotype.Service;

/**
 * @author Ivan97
 */
@Service
@Sharable
public class ProxyIdleHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    if (evt instanceof IdleStateEvent) {
      ctx.channel().close();
    } else {
      super.userEventTriggered(ctx, evt);
    }
  }
}
