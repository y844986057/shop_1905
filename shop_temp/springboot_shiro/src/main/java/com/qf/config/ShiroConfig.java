package com.qf.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.qf.realm.UserRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean//  <!-- 核心的组件 -->
    public DefaultWebSecurityManager securityManager(UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        securityManager.setSessionMode(true);
        securityManager.setRealm(userRealm);
//        securityManager.setCacheManager();
        return securityManager;
    }

//    @Bean
//    public UserRealm userRealm(){
//        return new UserRealm();
//    }


    @Bean // 用来来管理shirobean的生命周期
    public LifecycleBeanPostProcessor ShiroFilterFactoryBean(){
        return new LifecycleBeanPostProcessor();
    }

    @Bean//<!-- shiroFilter:很重要 -->
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //引入securityManager
        shiroFilterFactoryBean.setLoginUrl("toLogin");
        //shiro判断到没有登录后跳转的登录地址
        shiroFilterFactoryBean.setSuccessUrl("index.html");
        //登录成功后跳转的地址
        shiroFilterFactoryBean.setUnauthorizedUrl("noUnauthorized.html");
        //没有权限跳转的地址
        Map<String,String> map = new HashMap<String ,String>();
        map.put("/logout","logout");
        map.put("/index.html","anon");
        map.put("/hello","anon");
        map.put("/toLogin","anon");
        map.put("/login","anon");
        map.put("/**","authc"); // user

        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        // 过滤器 authc:必须要认证他通过后才可以调用 anon:匿名拦截器 logout:注销
        return shiroFilterFactoryBean;
    }

    @Bean//<!-- thymeleaf解析Shiro标签 -->
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    @Bean//<!-- 启动AOP代理 -->
    public static DefaultAdvisorAutoProxyCreator create(){
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }
    @Bean//<!-- 启动shiro注解 -->
    public AuthorizationAttributeSourceAdvisor sourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

}
