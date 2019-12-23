package com.derveljun.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NettyServerBootstrap {

    @Value("${netty.prop.event.group.boss.size}")
    int bossGroupSize;

    @Value("${netty.prop.event.group.worker.size}")
    int workerGroupSize;

    @Value("${netty.prop.server.port}")
    int nettyPort;

    private final NettyServerChildHandlerInitializer nettyServerChildHandlerInitializer;

    @Autowired
    public NettyServerBootstrap(NettyServerChildHandlerInitializer initializer){
        nettyServerChildHandlerInitializer = initializer;
    }

    public void run() throws InterruptedException {
        boolean isEpollAvailable = Epoll.isAvailable();

        log.info(" >> bossGroupSize : " + bossGroupSize);
        log.info(" >> workerGroupSize : " + workerGroupSize);
        log.info(" >> epoll available : " + isEpollAvailable);

        EventLoopGroup bossGroup = new NioEventLoopGroup(bossGroupSize);
        EventLoopGroup workerGroup = new NioEventLoopGroup(workerGroupSize);
        if (isEpollAvailable){
            bossGroup = new EpollEventLoopGroup(bossGroupSize);
            workerGroup = new EpollEventLoopGroup(workerGroupSize);
        }

        try {
            ServerBootstrap boot = new ServerBootstrap();
            boot.group(bossGroup, workerGroup)
                    .channel(isEpollAvailable ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .childHandler(nettyServerChildHandlerInitializer);

            ChannelFuture future = boot.bind(nettyPort).sync();
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
