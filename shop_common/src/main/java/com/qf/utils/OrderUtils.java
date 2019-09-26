package com.qf.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class OrderUtils {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 规则(yyyyMMddxxxxs)
     * 创建订单Id
     * @return
     */
    public String createOrdereId(Integer userId){

        StringBuffer buffer = new StringBuffer();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(new Date());

        buffer.append(date); // 添加当前时间

        // 获取用户Id的后四位
        String userIdEnd = getUserIdEnd(userId);
        buffer.append(userIdEnd); // 添加用户Id后四位

        // 添加流水号
        Object order_num = redisTemplate.opsForValue().get("order_num");// 先判断这个流水号是否存在
        if(order_num == null){
            redisTemplate.opsForValue().set("order_num","0");
        }
        Long orderNum = redisTemplate.opsForValue().increment("order_num");

        buffer.append(orderNum); // 添加流水号

        return buffer.toString();
    }

    /**
     * 截取用户Id的后四位(如果不足四位补0)
     * @param userId
     * @return
     */
    public String getUserIdEnd(Integer userId) {

        String userIdTemp =userId+"";

        StringBuffer buffer= new StringBuffer();

        // 1000
        // 1000
        // 先判断userId是否大于四位
        if(userIdTemp.length()<4){
            buffer.append(userId);
            for(int i=0;i<(4-userIdTemp.length());i++){
                buffer.insert(0,"0"); // 往前补0
            }
        }else{
           buffer.append(userIdTemp.substring(userIdTemp.length() - 4));
        }
        return buffer.toString();
    }

    public static void main(String[] args) {
        System.out.println(new OrderUtils().getUserIdEnd(12));
        System.out.println(new OrderUtils().getUserIdEnd(12345678));
    }

    public  Integer getUserIdByOid(String oid) {
        return Integer.parseInt(oid.substring(8,12));
    }
}
