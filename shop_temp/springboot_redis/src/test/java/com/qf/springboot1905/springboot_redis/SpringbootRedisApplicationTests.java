package com.qf.springboot1905.springboot_redis;

import ch.qos.logback.core.util.TimeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootRedisApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void contextLoads() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("name","张三");
        System.out.println(valueOperations.get("name"));
        System.out.println("=================================");

        HashOperations hashOperations = redisTemplate.opsForHash();
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("name","李四");
        hashMap.put("age","14");
        hashOperations.putAll("user",hashMap);
        System.out.println(hashOperations.get("user","name"));
        System.out.println(hashOperations.get("user","age"));
        System.out.println("=================================");

        ListOperations listOperations = redisTemplate.opsForList();
        listOperations.leftPushAll("phone","huawei","zte","sanxin","oppo","vivo");
        System.out.println(listOperations.range("phone",0,-1));
        System.out.println("=================================");

        SetOperations setOperations = redisTemplate.opsForSet();
        setOperations.add("email","huawei@qq.com","zte@qq.com","qf@qq.com");
        System.out.println(setOperations.members("email"));
        System.out.println("=================================");

        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add("score","张三",15.5);
        zSetOperations.add("score","李四",14.5);
        zSetOperations.add("score","王五",12.5);
        System.out.println(zSetOperations.range("score",0,-1));
    }

    @Test
    public void textTransaction(){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
           try {
                redisOperations.multi();
                redisOperations.opsForValue().set("name","zs");
                //int i=10/0;
                redisOperations.opsForValue().set("age",12);
                redisOperations.exec();
           }catch (Exception e){
               redisOperations.discard();
               e.printStackTrace();
           }


                return null;
            }
        });
    }


    @Test
    public void lsxTest(){
        long l = System.currentTimeMillis();
        for (int i=0;i<10000;i++){
            redisTemplate.opsForValue().set("key"+i,i);
        }
        long b = System.currentTimeMillis();
        System.out.println(b-l);
    }
    @Test
    public void lsxTest2(){
        long l = System.currentTimeMillis();

      redisTemplate.execute(new SessionCallback() {
          @Override
          public Object execute(RedisOperations redisOperations) throws DataAccessException {
              for (int i=0;i<10000;i++){
                  redisOperations.opsForValue().set("key"+i,i);
              }
              return null;
          }
      });

        long b = System.currentTimeMillis();
        System.out.println(b-l);
    }


    @Test
    public void TimeTest(){

        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                redisOperations.opsForValue().set("name","zs",20, TimeUnit.SECONDS);
                Long expire = redisOperations.getExpire("name");
                System.out.println(expire);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                expire = redisOperations.getExpire("name");
                System.out.println(expire);
                redisOperations.expire("name",50,TimeUnit.SECONDS);
                expire = redisOperations.getExpire("name");
                System.out.println(expire);
                redisOperations.persist("name");
                expire = redisOperations.getExpire("name");
                System.out.println(expire);
                return null;
            }
        });
    }
}
