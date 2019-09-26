package com.qf.service.impl.impl;

import com.qf.dao.IUserDao;
import com.qf.entity.User;
import com.qf.service.impl.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class springUserServiceImpl implements IUserService {

    @Autowired
    private IUserDao userDao;


    @Cacheable(value ="user" ,key ="'id_'+#id" )
    @Override
    public User getUserById(Integer id) {

        return userDao.selectByPrimaryKey(id);
    }

    @Cacheable(value = "user",key = "'List'")
    @Override
    public List<User> getUserList() {
        System.out.println("查询数据库了");
            return userDao.select(null);
    }

    @CacheEvict(value = "user",key = "'List'")
    @CachePut(value = "user",key = "'id_'+#user.id")
    @Override
    public User addUser(User user) {
        userDao.insertSelective(user);
        return user;
    }

    @CacheEvict(value = "user",key = "'List'")
    @Override
    public int deleteById(Integer id) {
        return userDao.deleteByPrimaryKey(id);
    }

    @Caching(evict = {
            @CacheEvict(value = "user",key = "'List'"),
            @CacheEvict(value = "user",key = "'id_'+#user.id")
    })
    @Override
    public int updateUser(User user) {
        return userDao.updateByPrimaryKeySelective(user);
    }
}
