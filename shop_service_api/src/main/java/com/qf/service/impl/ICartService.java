package com.qf.service.impl;

import com.qf.entity.Cart;
import com.qf.entity.User;

import java.util.List;

public interface ICartService extends IBaseService<Cart> {
    void addCart(String cartToken, User user, Cart cart);
    int findCartCount(String cartToken, User user);
    List<Cart> getCartList(String cartToken, User user);
    int deleteCart(Cart cart,User user,String cartToken);
    void updateCart(Cart cart, User user, String cartToken);

    int mergeCart(String cartToken, User user);

    void deleteCartByUserId(Integer id);
}
