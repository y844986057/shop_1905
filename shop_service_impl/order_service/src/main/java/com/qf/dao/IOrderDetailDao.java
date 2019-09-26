package com.qf.dao;

import com.qf.entity.OrderDetail;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface IOrderDetailDao extends Mapper<OrderDetail> {
    int batchAddOrderDetail(@Param("list") List<OrderDetail> orderDetails, @Param("tableNum") Integer tableNum);
}
