package tech.iooo.coco.commons.log;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author Ivan97
 */
public interface ProxyFlowLog {

  void log(ChannelHandlerContext ctx);
}
