package com.derveljun.netty;

import com.derveljun.netty.server.NettyServerBootstrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
public class NettyServerApplication {

    public static void main(String[] args) throws InterruptedException {

        log.info("Application Started");

        ConfigurableApplicationContext context = SpringApplication.run(NettyServerApplication.class, args);
        NettyServerBootstrap bootstrap = context.getBean(NettyServerBootstrap.class);
        bootstrap.run();

        log.info("Application Ended");
    }

}
