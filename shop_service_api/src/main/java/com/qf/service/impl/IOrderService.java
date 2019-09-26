package com.qf.service.impl;

import com.qf.entity.Order;
import com.qf.entity.User;

import java.util.List;

public interface IOrderService extends IBaseService<Order> {
    public String addOrder(Integer aId, User user);

    public Integer setDataSource(Integer userId);

    List<Order> getOrderListByUserId(Integer id);

    Order getOrderById(String oid);

    void updateOrderState(String out_trade_no, int state);
}
