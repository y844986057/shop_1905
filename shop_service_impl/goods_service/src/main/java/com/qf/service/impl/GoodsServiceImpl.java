package com.qf.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.dao.IGoodsDao;
import com.qf.entity.Goods;
import com.qf.entity.Page;
import com.qf.service.impl.IGoodsService;
import com.qf.service.impl.impl.BaseServiecImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Service
public class GoodsServiceImpl extends BaseServiecImpl<Goods> implements IGoodsService {

    @Autowired
    private IGoodsDao goodsDao;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    protected Mapper<Goods> getMapper() {
        return goodsDao;
    }

    @Override
    public PageInfo<Goods> getSearchPage(Page page, String keyWorld) {
        PageHelper.startPage(page.getPageNum(),page.getPageSize());
        List<Goods> list =goodsDao.getSearchList(keyWorld);
        return new PageInfo<Goods>(list);
    }

    @Override
    public int add(Goods goods) {
        int insert = goodsDao.insertSelective(goods);
        rabbitTemplate.convertAndSend("goods_exchange","",goods);
            return insert;
    }
}
