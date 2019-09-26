package com.qf.dao;

import com.qf.entity.Goods;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface IGoodsDao extends Mapper<Goods>{
    List<Goods> getSearchList(String keyWorld);
}
