package com.qf.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("toRegister").setViewName("register");
        registry.addViewController("toLogin").setViewName("login");
        registry.addViewController("toUpdatePassword").setViewName("updatePassword");
        registry.addViewController("toChangePassword").setViewName("changePassword");
    }
}
