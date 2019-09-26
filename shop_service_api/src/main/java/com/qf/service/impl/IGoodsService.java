package com.qf.service.impl;

import com.github.pagehelper.PageInfo;
import com.qf.entity.Goods;
import com.qf.entity.Page;

public interface IGoodsService extends  IBaseService<Goods> {
    PageInfo<Goods> getSearchPage(Page page, String keyWorld);
}
