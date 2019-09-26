package com.qf.service.impl;

import com.qf.entity.User;

import java.util.List;

public interface IUserService {

    User getUserById(Integer id);
    List<User> getUserList();
    User addUser(User user);
    int updateUser(User user);
    int deleteById(Integer id);
}
