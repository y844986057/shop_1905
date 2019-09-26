package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.aop.IsLogin;
import com.qf.entity.Cart;
import com.qf.entity.Constant;
import com.qf.entity.User;
import com.qf.service.impl.ICartService;
import com.qf.service.impl.IGoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RequestMapping(value = "/cart")
@Controller
public class CartController {

    @Reference
    private ICartService cartService;

    @Reference
    private IGoodsService goodsService;

    @RequestMapping(value = "/addCart")
    @IsLogin
    @ResponseBody
    public String addCart(@CookieValue(name = Constant.CART_TOKEN,required = false)String cartToken, HttpServletResponse response, User user, Cart cart){
        if (StringUtils.isEmpty(cartToken)){
                cartToken= UUID.randomUUID().toString();
            Cookie cookie = new Cookie(Constant.CART_TOKEN, cartToken);
            cookie.setMaxAge(60*60*24*5);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        }

        cartService.addCart(cartToken,user,cart);
        return "ok";
    }

    @RequestMapping(value = "/findCartCount")
    @ResponseBody
    @IsLogin
    public String findCartCount(User user,@CookieValue(name = Constant.CART_TOKEN,required = false) String cartToken,String callback){
        int count = cartService.findCartCount(cartToken,user);
        return callback != null?callback+"('"+count+"')":count+"";
    }



    @RequestMapping(value = "/getCartList")
    @IsLogin
    public String getCartList(User user, @CookieValue(name = Constant.CART_TOKEN,required = false) String cartToken, ModelMap map){
        List<Cart> cartList = cartService.getCartList(cartToken,user);
        Set<Integer> set = new HashSet<Integer>();
        Map<Integer,Cart> cartMap= new HashMap<>();
        for(Cart cart:cartList){
            if(set.add(cart.getGid())){
                cartMap.put(cart.getGid(),cart);
            }else{

                Integer num = cartMap.get(cart.getGid()).getNum(); // 之前商品数量
                cart.setNum(num+cart.getNum()); // 算出新的数量
                cart.setSubTotal(cart.getNum()*cart.getGoods().getGprice());
                cartMap.put(cart.getGid(),cart); // 覆盖之前的购物车对象
            }
        }

        if(!cartMap.isEmpty()){
            cartList.clear(); // 清空之前的集合
            Set<Map.Entry<Integer, Cart>> entries = cartMap.entrySet();
            for(Map.Entry<Integer, Cart> entri :entries){
                entri.getKey();
                cartList.add(entri.getValue()) ; // 把map中的数据放入到集合中
            }
        }

        // 算出总价
        Double totalPrice = 0.0;
        for(Cart cart:cartList){
            totalPrice+=cart.getSubTotal();
        }

        map.put("cartList",cartList);
        map.put("totalPrice",totalPrice);
        return "cartList";
    }


    @RequestMapping(value = "/subCart/{gid}")
    @IsLogin
    @ResponseBody
    public String subCart(@CookieValue(name = Constant.CART_TOKEN,required = false)String cartToken, HttpServletResponse response, User user,@PathVariable Integer gid,String callback){
        List<Cart> cartList = cartService.getCartList(cartToken,user);
        for (Cart cart1 : cartList) {
            if(gid.equals(cart1.getGid())){
                if (cart1.getNum()==1){
                    int i = cartService.deleteCart(cart1, user, cartToken);
                    break;
                }else {
                    cart1.setNum(cart1.getNum()-1);
                    cart1.setSubTotal(cart1.getNum()*cart1.getGoods().getGprice());
                    cartService.updateCart(cart1,user,cartToken);
                    break;
                }
            }
        }
        return callback!=null?callback+"('ok')":"ok";
    }

    @RequestMapping(value = "/addCart2")
    @IsLogin
    @ResponseBody
    public String addCart2(@CookieValue(name = Constant.CART_TOKEN,required = false)String cartToken, HttpServletResponse response, User user, Cart cart,String callback){
        List<Cart> cartList = cartService.getCartList(cartToken,user);
        for (Cart cart1 : cartList) {
           if(cart.getGid().equals(cart1.getGid())){
               cart1.setNum(cart1.getNum()+1);
               cart1.setSubTotal(cart1.getNum()*cart1.getGoods().getGprice());
               cartService.updateCart(cart1,user,cartToken);
               break;
           }
        }
        return callback!=null?callback+"('ok')":"ok";
    }

    @RequestMapping(value = "/deleteCart/{gid}")
    @IsLogin
    public String deleteCart(@CookieValue(name = Constant.CART_TOKEN,required = false)String cartToken, HttpServletResponse response, User user,@PathVariable Integer gid){
        System.out.println(gid);
        List<Cart> cartList = cartService.getCartList(cartToken, user);
        for (Cart cart1 : cartList) {
            if (gid.equals(cart1.getGid())){
                int i = cartService.deleteCart(cart1, user, cartToken);
            }
        }
        return "redirect:../getCartList";
    }
}
