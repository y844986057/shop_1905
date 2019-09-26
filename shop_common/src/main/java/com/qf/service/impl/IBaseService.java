package com.qf.service.impl;

import com.github.pagehelper.PageInfo;

import java.util.List;

public interface IBaseService<T> {

    public int add(T t);

    public int udpate(T t);

    public T getById(Integer id);

    public int delete(Integer id);

    public List<T> getList();

    public PageInfo<T> getPage(Integer pageNum, Integer pageSize);
}
