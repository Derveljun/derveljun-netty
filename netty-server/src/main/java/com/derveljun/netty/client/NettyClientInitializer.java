package com.derveljun.netty.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    private final int timeout;
    private NettyClientHandler nettyClientHandler;

    public NettyClientInitializer(NettyClientHandler handler, int timeout){
        this.nettyClientHandler = handler;
        this.timeout = timeout;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(new ReadTimeoutHandler(timeout), nettyClientHandler);
    }
}
