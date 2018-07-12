package tech.iooo.coco.commons.component;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import lombok.Getter;
import tech.iooo.coco.commons.log.ProxyFlowLog;

/**
 * @author Ivan97
 */
public class ProxyChannelTrafficShapingHandler extends ChannelTrafficShapingHandler {

  public static final String PROXY_TRAFFIC = "ProxyChannelTrafficShapingHandler";

  @Getter
  private long beginTime;
  @Getter
  private long endTime;
  @Getter
  private String username = "ANONYMOUS";

  private ProxyFlowLog proxyFlowLog;

  private ChannelListener channelListener;

  public ProxyChannelTrafficShapingHandler(long checkInterval, ProxyFlowLog proxyFlowLog,
      ChannelListener channelListener) {
    super(checkInterval);
    this.proxyFlowLog = proxyFlowLog;
    this.channelListener = channelListener;
  }

  public static ProxyChannelTrafficShapingHandler get(ChannelHandlerContext ctx) {
    return (ProxyChannelTrafficShapingHandler) ctx.pipeline().get(PROXY_TRAFFIC);
  }

  public static void username(ChannelHandlerContext ctx, String username) {
    get(ctx).username = username;
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    beginTime = System.currentTimeMillis();
    if (channelListener != null) {
      channelListener.active(ctx);
    }
    super.channelActive(ctx);
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    endTime = System.currentTimeMillis();
    if (channelListener != null) {
      channelListener.inActive(ctx);
    }
    proxyFlowLog.log(ctx);
    super.channelInactive(ctx);
  }
}
