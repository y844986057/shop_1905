package com.qf.service.impl;


import com.qf.entity.Goods;

import java.util.List;

public interface ISearchService {

    List<Goods> getKeyWorldList(String keyWorld);

    void add(Goods goods);
}
