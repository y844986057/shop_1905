package com.qf.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.dao.ICartDao;
import com.qf.entity.Cart;
import com.qf.entity.Goods;
import com.qf.entity.User;
import com.qf.service.impl.ICartService;
import com.qf.service.impl.IGoodsService;
import com.qf.service.impl.impl.BaseServiecImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import tk.mybatis.mapper.common.Mapper;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class CartServiceImpl extends BaseServiecImpl<Cart> implements ICartService {

    @Autowired
    private ICartDao cartDao;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    protected Mapper<Cart> getMapper() {
        return cartDao;
    }

    @Override
    public void addCart(String cartToken, User user, Cart cart) {
        cart.setCreateTime(new Date());
        Goods goods = goodsService.getById(cart.getGid());
        cart.setSubTotal(cart.getNum()*goods.getGprice());

        if (user!=null){
            cart.setUid(user.getId());
            cartDao.insert(cart);
        }else {
            redisTemplate.opsForList().rightPush(cartToken,cart);
            redisTemplate.expire(cartToken,5, TimeUnit.DAYS);
        }


    }

    @Override
    public int findCartCount(String cartToken, User user) {
        int count=0;
        if (user!=null){
            Cart cart = new Cart();
            cart.setUid(user.getId());
             count = cartDao.selectCount(cart);
        }else if(cartToken!=null){
             count = redisTemplate.opsForList().size(cartToken).intValue();
        }
        return count;
    }

    @Override
    public List<Cart> getCartList(String cartToken, User user) {
        List<Cart> cartList=null;
        if (user!=null){
            Cart cart = new Cart();
            cart.setUid(user.getId());
            cartList = cartDao.select(cart);
        }else if (cartToken!=null){
             cartList = redisTemplate.opsForList().range(cartToken, 0, -1);
        }
        // 查询具体商品信息
        for(Cart cart :cartList){
            Goods goods = goodsService.getById(cart.getGid());
            cart.setGoods(goods);
            cart.setSubTotal(cart.getNum()*goods.getGprice());
        }
        return cartList;
    }

    @Override
    public int deleteCart(Cart cart,User user,String cartToken) {
        List<Cart> cartList=null;
        if (user!=null){
            cartDao.deleteByPrimaryKey(cart);
        return 1;
        }else {
            cartList = redisTemplate.opsForList().range(cartToken, 0, -1);
                redisTemplate.delete(cartToken);
            System.out.println("清空缓存");
            for (Cart cart1 : cartList) {
                if (cart1.getGid().equals(cart.getGid())&&cart1.getNum().equals(cart.getNum())){
                    cartList.remove(cart1);
                    break;
                }
            }
            if (cartList.size()!=0) {
                redisTemplate.opsForList().rightPushAll(cartToken, cartList);
                redisTemplate.expire(cartToken, 5, TimeUnit.DAYS);
            }
            return 2;
        }
    }

    @Override
    public void updateCart(Cart cart, User user, String cartToken) {
        List<Cart> cartList=null;
        if (user!=null){
           cartDao.updateByPrimaryKeySelective(cart);
        }else {
            cartList = redisTemplate.opsForList().range(cartToken, 0, -1);
            redisTemplate.delete(cartToken);
            for (Cart cart1 : cartList) {
                if (cart.getGid().equals(cart1.getGid())){
                    cart1.setNum(cart.getNum());
                    break;
                }

            } redisTemplate.opsForList().rightPushAll(cartToken,cartList);
            redisTemplate.expire(cartToken,5, TimeUnit.DAYS);
        }
    }

    @Override
    public int mergeCart(String cartToken, User user) {

        List<Cart> cartList= (List<Cart>) redisTemplate.opsForList().range(cartToken, 0, -1);
        if (cartList.size()>0){
            for (Cart cart : cartList) {
                cart.setUid(user.getId());
                cartDao.insert(cart);
            }
            redisTemplate.delete(cartToken);
            return 1;
        }
        return 0;
    }

    @Override
    public void deleteCartByUserId(Integer id) {
        Cart cart = new Cart();
        cart.setUid(id);
        cartDao.delete(cart);
    }


}
