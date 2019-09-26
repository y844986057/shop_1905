package com.qf.dao;

import com.qf.entity.User;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface IUserDao  extends Mapper<User>{
    List<User> getUserList(User user);

    User getUserByEmail(String email);

    User getUserByUsername(String username);
}
