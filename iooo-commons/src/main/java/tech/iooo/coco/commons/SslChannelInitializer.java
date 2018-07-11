package tech.iooo.coco.commons;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import javax.net.ssl.SSLEngine;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 2018/4/20 下午4:10
 *
 * @author Ivan97
 */
@Sharable
@AllArgsConstructor
public class SslChannelInitializer extends ChannelInitializer<Channel> {

    private static final Logger logger = LoggerFactory.getLogger(SslChannelInitializer.class);
    private final SslContext sslContext;

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        SSLEngine engine = sslContext.newEngine(ch.alloc());
        pipeline.addFirst(new SslHandler(engine, true));
    }
}
