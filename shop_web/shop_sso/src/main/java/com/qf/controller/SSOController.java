package com.qf.controller;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.qf.entity.Constant;
import com.qf.entity.Email;
import com.qf.entity.ResultEntity;
import com.qf.entity.User;
import com.qf.service.impl.ICartService;
import com.qf.service.impl.IUserService;
import com.qf.utils.PasswordUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping(value = "/sso")
public class SSOController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Reference
    private IUserService userService;

    @Reference
    private ICartService cartService;

//    @Reference
//    private IEmailService emailService;

    @RequestMapping(value = "/sendEmail")
    @ResponseBody
    public void sendEmail(String email){
        int code = (int)(Math.random()*9000)+1000;

        String content = "验证码:%d,如果非本人操作，请忽略.";
        content = String.format(content,code);

        redisTemplate.opsForValue().set(email + Constant.EMAIL_CODE, code, 30, TimeUnit.SECONDS);

        Email email1 = new Email("码农网注册验证码",content,email);
        System.out.println("邮件发送的"+code);
        rabbitTemplate.convertAndSend(Constant.EMAILEXCHANGE,"",email1);

    }


    @RequestMapping(value = "/register")
    @ResponseBody
    public ResultEntity<String> register(User user, Integer code) {
        System.out.println("我写的验证码"+code);

        Integer redisCode = (Integer) redisTemplate.opsForValue().get(user.getEmail() + Constant.EMAIL_CODE);

        if (redisCode != null && redisCode.equals(code)) {

            int state = userService.register(user);
            if (state == 1) {
                return ResultEntity.FAILD("邮箱已经被注册");
            } else if (state == 2) {
                return ResultEntity.FAILD("用户名已经被注册");
            } else if (state == 3) {
                return ResultEntity.SEUCCESS_URL("http://localhost:8084/toLogin");
            }
        }
        return ResultEntity.FAILD("验证码失效");
    }
    @RequestMapping(value = "/findEmailByUsername")
    public String findEmailByUsername(String username, Model model) {

        User user = userService.getUserByUsername(username);
        if (user != null) {

            String email = user.getEmail();
            String emailTemp = email.replace(email.substring(4, email.indexOf("@")), "*****");
            String emailUrl = email.replace(email.substring(0, email.indexOf("@") + 1), "mail.");
            String toke = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set(username + Constant.PWTOKEN, toke, 5, TimeUnit.MINUTES);

            String passwordUrl = "http://127.0.0.1:8084/toChangePassword?username=" + username + "&token=" + toke;

            Email email1Entity = new Email();
            email1Entity.setTilte("码农网密码修改");
            email1Entity.setContent("密码修改连接:<a href = '" + passwordUrl + "'>点击这里这里修改</a>");
            email1Entity.setTo(email);

            rabbitTemplate.convertAndSend(Constant.EMAILEXCHANGE, "", email1Entity);

            model.addAttribute("msg", "连接已发送到" + emailTemp + "的邮箱，请<a href='http://" + emailUrl + "'>登录</a>");
        } else {
            model.addAttribute("msg", "该【" + username + "】不存在");
        }

        return "updatePassword";
    }

    @RequestMapping(value = "/changePassword")
    public String changePassword(String newPassword, String username, String token) {
        System.out.println("SSOController.changePassword");
        String redisToke = (String) redisTemplate.opsForValue().get(username + Constant.PWTOKEN);
        if (redisToke != null && redisToke.equals(token)) {
            User user = userService.getUserByUsername(username);
            user.setPassword(newPassword);
            userService.udpate(user);
            redisTemplate.delete(username + Constant.PWTOKEN);
            return "redirect:/toLogin";
        }
        return "error";
    }

    @RequestMapping(value = "/login")
    @ResponseBody
    public ResultEntity<String> login(String returnUrl,String username, String password, HttpServletResponse response,@CookieValue(name = Constant.CART_TOKEN,required = false)String cartToken) {

        User user = userService.login(username);

        if (user != null) {

            if (PasswordUtils.checkpw(password, user.getPassword())) {
                if(!StringUtils.isEmpty(cartToken)) {
                    int stats = cartService.mergeCart(cartToken, user);
                    if (stats > 0) {
                        Cookie cookie = new Cookie(Constant.CART_TOKEN, "");
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                    }
                }
                String loginToken = UUID.randomUUID().toString();
                redisTemplate.opsForValue().set(loginToken, user, 5, TimeUnit.DAYS);
                Cookie cookie = new Cookie(Constant.LOGIN_TOKEN, loginToken);
                cookie.setPath("/");
                cookie.setMaxAge(60 * 60 * 24 * 5);
                response.addCookie(cookie);

                if (returnUrl.isEmpty()){
                    returnUrl="http://localhost:8081/";
                }

                return ResultEntity.SEUCCESS_URL(returnUrl);
            } else {
                return ResultEntity.FAILD("用户名或密码错误");
            }
        } else {
            return ResultEntity.FAILD("用户名不存在");
        }
    }

    @RequestMapping(value = "/logout")
    public String logout(@CookieValue(name = Constant.LOGIN_TOKEN,required = false) String LoginToken,HttpServletResponse response){
        if (LoginToken!=null){
            redisTemplate.delete(LoginToken);
            Cookie cookie = new Cookie(Constant.LOGIN_TOKEN, "");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

        return "redirect:../toLogin";
    }

    @RequestMapping(value = "/checkLogin")
    @ResponseBody
    public String checkLogin(@CookieValue(name = Constant.LOGIN_TOKEN,required = false)String LoginToken,String callback){
            String userJSON="";
            if (LoginToken!=null){
                User user = (User) redisTemplate.opsForValue().get(LoginToken);
               if (user!=null) {
                   Gson gson = new Gson();
                   userJSON = gson.toJson(user);
               }
            }
        return callback!=null?callback+"('"+userJSON+"')":userJSON;
    }
}
