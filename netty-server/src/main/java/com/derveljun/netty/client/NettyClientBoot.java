package com.derveljun.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class NettyClientBoot {

    public String start(String ip, int port, byte[] sendMsg, int timeoutSecond) throws InterruptedException {
        String resMsg = "";
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            final NettyClientHandler clientHandler = new NettyClientHandler(sendMsg);
            NettyClientInitializer clientInitializer = new NettyClientInitializer(clientHandler, 30);

            Bootstrap boot = new Bootstrap();
            boot.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .remoteAddress(new InetSocketAddress(ip, port))
                    .handler(clientInitializer);

            ChannelFuture future = boot.connect().sync();
            future.channel()
                    .closeFuture()
                    .sync();

            resMsg = clientHandler.readResponseMessage();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

        return resMsg;
    }

}
