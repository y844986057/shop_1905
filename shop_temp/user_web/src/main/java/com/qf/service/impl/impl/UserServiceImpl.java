package com.qf.service.impl.impl;

import com.qf.dao.IUserDao;
import com.qf.entity.User;
import com.qf.service.impl.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserDao userDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public User getUserById(Integer id) {

        return userDao.selectByPrimaryKey(id);
    }


    @Override
    public List<User> getUserList() {
        List<User> userList = null;
        userList= (List<User>) redisTemplate.opsForValue().get("userList");
        if (userList==null){
            Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", "true", 60, TimeUnit.SECONDS);
            if(lock){
                System.out.println("查数据库了");
                userList= userDao.select(null);
                redisTemplate.opsForValue().set("userList",userList,60, TimeUnit.SECONDS);
                redisTemplate.delete("lock");
            }else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return getUserList();
            }
        }
            return userList;
    }

    @Override
    public User addUser(User user) {
        redisTemplate.delete("userList");
        userDao.insertSelective(user);
        return user;
    }

    @Override
    public int updateUser(User user) {
        redisTemplate.delete("userList");
        return userDao.updateByPrimaryKeySelective(user);
    }

    @Override
    public int deleteById(Integer id) {
        redisTemplate.delete("userList");
        return userDao.deleteByPrimaryKey(id);
    }
}
