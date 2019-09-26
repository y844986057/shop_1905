package com.qf.springboot1905.shop_order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Address;
import com.qf.entity.Cart;
import com.qf.entity.Order;
import com.qf.entity.User;
import com.qf.service.impl.IAddressService;
import com.qf.service.impl.ICartService;
import com.qf.service.impl.IOrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopOrderApplicationTests {

    @Reference
    private IAddressService addressService;

    @Reference
    private ICartService cartService;

    @Reference
    private IOrderService orderService;
    @Test
    public void contextLoads() {
        List<Address> addressList = addressService.getAddressListByUid(29);
        for (Address address : addressList) {
            System.out.println(address);
        }
    }

    @Test
    public void contextLoads2() {
        User user = new User();
        user.setId(29);
        List<Cart> cartList = cartService.getCartList(null, user);

        Double totalPrice = 0.0;
        for(Cart cart:cartList){
            totalPrice+=cart.getSubTotal();
        }
        System.out.println(totalPrice);
    }
    @Test
    public void sssss(){
        User user = new User();
        user.setId(29);
        List<Order> orderList = orderService.getOrderListByUserId(user.getId());
        for (Order order : orderList) {
            System.out.println(order.getOrderDetailList().get(0).getGname());

        }
    }
    @Test
    public void ssss(){
        Order order = orderService.getOrderById("20190918002911");
        System.out.println(order.getOrderDetailList().get(0).getGname());
    }

}
