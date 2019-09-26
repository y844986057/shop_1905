package com.qf.dao;

import com.qf.entity.Address;
import com.qf.entity.Order;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface IOrderDao extends Mapper<Order> {
    void addOrder(@Param("order") Order order, @Param("tableNum") Integer tableNum);

    List<Order> getOrderListByUserIdAndTableNum(@Param("userId") Integer userId, @Param("tableNum") Integer tableNum);

    Order getOrderById(@Param("oid") String oid, @Param("tableNum") Integer tableNum);

    void updateOrderState(@Param("oid") String oid, @Param("state") int state, @Param("tableNum") Integer tableNum);
}