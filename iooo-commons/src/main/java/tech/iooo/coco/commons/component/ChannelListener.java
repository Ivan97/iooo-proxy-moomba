package tech.iooo.coco.commons.component;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author Ivan97
 */
public interface ChannelListener {

  void inActive(ChannelHandlerContext ctx);

  void active(ChannelHandlerContext ctx);
}
