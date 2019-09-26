package com.qf.springboot1905.goods_service;

import com.qf.config.ConnectionUtils;
import com.qf.dao.IGoodsDao;
import com.qf.entity.Goods;
import com.rabbitmq.client.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsServiceApplicationTests {


	@Autowired
	private IGoodsDao goodsDao;

	@Autowired
	private RedisTemplate redisTemplate;


	@Test
	public void contextLoads() {
		List<Goods> searchList = goodsDao.getSearchList("手机");
		for (Goods goods : searchList) {
			System.out.println(goods);
		}
	}
	private static ExecutorService executorService = Executors.newFixedThreadPool(10);

	@Test
	public void protest() throws IOException {
		Connection connection= ConnectionUtils.getConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare("myexchange","fanout");
		String msg="hello wolrd";
		channel.basicPublish("myexchange","",null,msg.getBytes("utf-8"));
		connection.close();
		System.out.println("发送完成");
	}
	@Test
	public void xfz1() throws IOException {
		Connection connection = ConnectionUtils.getConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare("xfz1",false,false,false,null);
		channel.queueBind("xfz1","myexchange","");
		channel.basicConsume("xfz1",true,new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				executorService.submit(new Runnable() {
					@Override
					public void run() {
						try {
							System.out.println("xfz1:"+new String(body,"utf-8"));
							Thread.sleep(2000);
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}
				});
			}
		});
	}
	@Test
	public void xfz2() throws IOException {
		Connection connection = ConnectionUtils.getConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare("xfz2",false,false,false,null);
		channel.queueBind("xfz2","myexchange","");
		channel.basicConsume("xfz2",true,new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				System.out.println("xfz2:"+new String(body,"utf-8"));
			}
		});
	}


	@Test
	public void listtest(){
		for (int i=0;i<=10;i++){
		redisTemplate.opsForList().rightPush("user",i);
		}
		redisTemplate.delete("user");
		List user = redisTemplate.opsForList().range("user",0,-1);
		for (Object o : user) {
			System.out.println(o);
		}
	}

}
