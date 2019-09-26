package com.qf.service.impl.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.dao.IUserDao;
import com.qf.entity.Page;
import com.qf.entity.User;
import com.qf.service.impl.IUserService;
import com.qf.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Service
public class UserServiceImpl extends BaseServiecImpl<User> implements IUserService {

    @Autowired
    private IUserDao userDao;

    @Override
    protected Mapper<User> getMapper() {
        return userDao;
    }

    @Override
    public PageInfo<User> getUserPage(Page page, User user) {

        PageHelper.startPage(page.getPageNum(),page.getPageSize());

        List<User> userList = userDao.getUserList(user); // 会做模糊查询
        return new PageInfo<User>(userList);
    }

    @Override
    public int register(User user) {

        // 1.判断邮箱是否别注册
        User u1 = userDao.getUserByEmail(user.getEmail());
        if(u1 != null){
            return 1; // 1邮箱已经被注册
        }

        // 2.判断用户名是否存在
        User u2 = userDao.getUserByUsername(user.getUsername());
        if(u2 != null){
            return 2; // 用户名称已存在
        }

        // 3.注册
        user.setPassword(PasswordUtils.hashpw(user.getPassword()));
        userDao.insert(user);

        return 3; // 代表注册成功
    }

    @Override
    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    @Override
    public User login(String username) {
        return this.getUserByUsername(username);
    }

    @Override
    public int add(User user) {
        user.setPassword(PasswordUtils.hashpw(user.getPassword())); // ?
        return super.add(user);
    }

    @Override
    public int udpate(User user) {
        user.setPassword(PasswordUtils.hashpw(user.getPassword()));
        return super.udpate(user);
    }
}
