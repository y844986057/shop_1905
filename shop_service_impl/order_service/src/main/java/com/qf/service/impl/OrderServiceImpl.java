package com.qf.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.qf.config.ThreadLocalDataSource;
import com.qf.dao.IOrderDao;
import com.qf.dao.IOrderDetailDao;
import com.qf.entity.*;
import com.qf.service.impl.impl.BaseServiecImpl;
import com.qf.utils.OrderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl extends BaseServiecImpl<Order> implements IOrderService {

    @Autowired
    private IOrderDao orderDao;

    @Autowired
    private OrderUtils orderUtils;

    @Autowired
    private IOrderDetailDao orderDetailDao;

    @Reference
    private IAddressService addressService;

    @Reference
    private ICartService cartService;

    @Override
    protected Mapper<Order> getMapper() {
        return orderDao;
    }


    @Override
    public String addOrder(Integer aId, User user) {

        Address address = addressService.getById(aId);
        List<Cart> cartList = cartService.getCartList(null, user);
        Double totalPrice = 0.0;
        for(Cart cart:cartList){
            totalPrice+=cart.getSubTotal();
        }
        String orderId = orderUtils.createOrdereId(user.getId());
        Order order = new Order();
        order.setAddress(address.getAddress());
        order.setPhone(address.getPhone());
        order.setUsername(address.getUsername());
        order.setUid(user.getId());
        order.setCreateTime(new Date());
        order.setTotalPrice(totalPrice);
        order.setId(orderId);
        order.setState(0);
        Integer tableNum = setDataSource(user.getId());

        orderDao.addOrder(order,tableNum);
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
        int i=0;
        for (Cart cart : cartList) {
            i++;
            Goods goods = cart.getGoods();
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setGdesc(goods.getGdesc());
            orderDetail.setGid(cart.getGid());
            orderDetail.setGname(goods.getGname());
            orderDetail.setGpic(goods.getGpic());
            orderDetail.setGprice(goods.getGprice());
            orderDetail.setNum(cart.getNum());
            orderDetail.setOid(orderId);
            orderDetail.setSubTotal(cart.getSubTotal());
            orderDetails.add(orderDetail);

            if (i%500==0||orderDetails.size()==500){
                orderDetailDao.batchAddOrderDetail(orderDetails,tableNum);
                orderDetails.clear();
            }
        }
        if (orderDetails.size()>0) {
            orderDetailDao.batchAddOrderDetail(orderDetails, tableNum);
        }

        cartService.deleteCartByUserId(user.getId());

        return orderId;
    }
    public Integer setDataSource(Integer userId) {
        Integer userIdEnd = Integer.parseInt(orderUtils.getUserIdEnd(userId));

        Integer dbNum = (userIdEnd % 2)+1;
        Integer tableNum = (userIdEnd/2 % 2)+1;

        ThreadLocalDataSource.set("order"+dbNum); // 插入那个数据库

        return tableNum;
    }

    @Override
    public List<Order> getOrderListByUserId(Integer userId) {
        Integer tableNum = setDataSource(userId);
        return orderDao.getOrderListByUserIdAndTableNum(userId,tableNum);
    }

    @Override
    public Order getOrderById(String oid) {
        Integer uid=orderUtils.getUserIdByOid(oid);

        Integer tableNum=setDataSource(uid);

        Order order = orderDao.getOrderById(oid, tableNum);

        return order;
    }

    @Override
    public void updateOrderState(String oid, int state) {
        Integer userId = orderUtils.getUserIdByOid(oid);
        Integer tableNum = setDataSource(userId);
        orderDao.updateOrderState(oid,state,tableNum);
    }
}
