package com.git.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.Ordered;

import com.git.template.general.netty.NettyServerListener;

/**
 * 注:CommandLineRunner代表在服务器启动后,执行run方法中的相关代码
 * 指定顺序的部分也可以使用 @Order接口
 *
 * @author wangp
 */
@SpringBootApplication
@EnableAspectJAutoProxy
public class TemplateApplication implements CommandLineRunner, Ordered {

    @Autowired
    private NettyServerListener listener;

    public static void main(String[] args) {
        SpringApplication.run(TemplateApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>>>>>>>>>>>start 0<<<<<<<<<<<");
        listener.start();
    }

    @Override
    public int getOrder() {
        return 0;
    }


    public String tS(String s) {
        return new String("x");
    }
}
