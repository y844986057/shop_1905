package com.qf.config;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectionUtils {

    private static ConnectionFactory factory=null;
    static {
        // 1.连接RamabbitMQ
        factory = new ConnectionFactory();
        factory.setHost("192.168.48.129"); // 连接地址
        factory.setPort(5672); // 设置端口号
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("/"); // 配置虚拟主机(每个团队用自己的消息队列)

    }

    public static Connection getConnection(){
        Connection connection = null;// 创建连接
        try {
            connection = factory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return connection;
    }

}
