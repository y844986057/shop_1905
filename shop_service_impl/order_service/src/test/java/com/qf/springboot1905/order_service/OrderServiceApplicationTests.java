package com.qf.springboot1905.order_service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Address;
import com.qf.entity.Order;
import com.qf.service.impl.IAddressService;
import com.qf.service.impl.IOrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceApplicationTests {

    @Reference
    private IAddressService addressService;
    @Reference
    private IOrderService orderService;

    @Test
    public void contextLoads() {
        List<Address> byUserIdAddressList = addressService.getAddressListByUid(29);
        for (Address address : byUserIdAddressList) {
            System.out.println(address);
        }
    }

    @Test
    public void getOrderList() {
        List<Order> orderList = orderService.getOrderListByUserId(29);
        for (Order order : orderList) {
            System.out.println(orderList);
        }

    }

}
