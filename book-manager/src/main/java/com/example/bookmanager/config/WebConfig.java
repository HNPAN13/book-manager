package com.example.bookmanager.config;

import com.example.bookmanager.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private LoginInterceptor loginInterceptor;

    //注册登录拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                // 放行：登录、生成密码测试接口
                .excludePathPatterns("/login", "/testPwd","/checkLogin","/logout","/book/add","/user/register");
    }
}
