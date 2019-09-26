package com.qf.service.impl;

import com.github.pagehelper.PageInfo;
import com.qf.entity.Page;
import com.qf.entity.User;

public interface IUserService extends IBaseService<User> {

    public PageInfo<User> getUserPage(Page page, User user);

    int register(User user);

    User getUserByUsername(String username);

    User login(String username);
}
