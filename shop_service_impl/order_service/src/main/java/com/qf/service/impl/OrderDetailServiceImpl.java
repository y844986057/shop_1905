package com.qf.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.dao.IOrderDetailDao;
import com.qf.entity.OrderDetail;
import com.qf.service.impl.impl.BaseServiecImpl;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;

@Service
public class OrderDetailServiceImpl extends BaseServiecImpl<OrderDetail> implements IOrderDetailService {

    @Autowired
    private IOrderDetailDao orderDetailDao;

    @Override
    protected Mapper<OrderDetail> getMapper() {
        return orderDetailDao;
    }



}
