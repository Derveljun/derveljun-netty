package com.derveljun.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NettyServerChildHandlerInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private NettyServerInboundHandler nettyServerInboundHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("STRING_DECODER", new StringDecoder(CharsetUtil.UTF_8))
                .addLast("STRING_ENCODER", new StringEncoder(CharsetUtil.UTF_8))
                .addLast("INBOUND_HANDLER", nettyServerInboundHandler);

    }
}
