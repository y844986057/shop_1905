package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.aop.IsLogin;
import com.qf.entity.Address;
import com.qf.entity.Cart;
import com.qf.entity.Order;
import com.qf.entity.User;
import com.qf.service.impl.IAddressService;
import com.qf.service.impl.ICartService;
import com.qf.service.impl.IOrderService;
import com.qf.utils.OrderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@RequestMapping(value = "order")
@Controller
public class OrederController {
    @Reference
    private IAddressService addressService;

    @Reference
    private ICartService cartService;

    @Autowired
    private OrderUtils orderUtils;

    @Reference
    private IOrderService orderService;
    @RequestMapping(value = "/toOrder")
    @IsLogin(mustUser = true)
    public String toOrder(User user, ModelMap map){
        List<Address> addressList = addressService.getAddressListByUid(user.getId());
        List<Cart> cartList = cartService.getCartList(null, user);
        Double totalPrice = 0.0;
        for(Cart cart:cartList){
            totalPrice+=cart.getSubTotal();
        }
        map.put("addressList",addressList);
        map.put("cartList",cartList);
        map.put("totalPrice",totalPrice);
        return "toOrder";
    }

    @IsLogin(mustUser = true)
    @RequestMapping(value = "/addOrder")
    public String addOrder(Integer aId,User user){
        String orderId=orderService.addOrder(aId,user);

        return "redirect:/pay?oid="+orderId;
    }
    @RequestMapping(value = "/getOrderList")
    @IsLogin(mustUser = true)
    public String getOrderList(User user,ModelMap map){
        List<Order> orderList = orderService.getOrderListByUserId(user.getId());
        map.put("orderList",orderList);

        return "orderList";
    }
}
