package com.qf.aop;

import com.qf.entity.Constant;
import com.qf.entity.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLEncoder;

@Aspect
@Component
public class LoginAop {

    @Autowired
    private RedisTemplate redisTemplate;


    @Around("@annotation(IsLogin)")
    public Object hendlerLogin(ProceedingJoinPoint point){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String loginToken="";
        Cookie[] cookies = request.getCookies();
        if (cookies!=null) {
            for (Cookie cookie : cookies) {
                if (Constant.LOGIN_TOKEN.equals(cookie.getName())){
                    loginToken=cookie.getValue();
                }
            }
        }
        User user=null;
        if (!StringUtils.isEmpty(loginToken)) {
            user = (User) redisTemplate.opsForValue().get(loginToken);
        }


        if (user==null) {
            MethodSignature methodSignature = (MethodSignature) point.getSignature();
            Method method = methodSignature.getMethod();
            IsLogin isLogin = method.getAnnotation(IsLogin.class);
            if (isLogin.mustUser()) {
                String url = request.getRequestURL().toString();
                try {
                    url= URLEncoder.encode(url,"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return "redirect:http://localhost:8084/toLogin?returnUrl="+ url;
            }
        }


        Object[] args = point.getArgs();
        for(int i =0;i<args.length;i++){
            if(args[i]!= null && args[i].getClass() == User.class){
                args[i]=user;
                break;
            }
        }

        Object proceed="";
        try {
             proceed = point.proceed(args);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return  proceed;
    }



}
