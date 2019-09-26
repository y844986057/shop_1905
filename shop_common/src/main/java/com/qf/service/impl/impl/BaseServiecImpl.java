package com.qf.service.impl.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.service.impl.IBaseService;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public abstract class BaseServiecImpl<T> implements IBaseService<T> {
    protected abstract Mapper<T> getMapper();

    @Override
    public int add(T t) {
        return getMapper().insert(t);
    }

    @Override
    public int udpate(T t) {
        return getMapper().updateByPrimaryKeySelective(t);
    }

    @Override
    public T getById(Integer id) {
        return getMapper().selectByPrimaryKey(id);
    }

    @Override
    public int delete(Integer id) {
        return getMapper().deleteByPrimaryKey(id);
    }

    @Override
    public List<T> getList() {
        return getMapper().select(null);
    }

    @Override
    public PageInfo<T> getPage(Integer pageNum, Integer pageSize) {

        // 1.设置分页参数
        PageHelper.startPage(pageNum,pageSize);

        // 2.查询当前页的数据
        List<T> list = getMapper().select(null);

        return new PageInfo<T>(list);
    }
}
