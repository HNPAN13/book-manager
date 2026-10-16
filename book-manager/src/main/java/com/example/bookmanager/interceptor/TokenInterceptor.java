package com.example.bookmanager.interceptor;

import com.example.bookmanager.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //打印请求uri
        String uri = request.getRequestURI();
        System.out.println("====拦截器进入，请求地址uri：" + uri);

        // 白名单：登录、注册直接放行，不校验token
        if ("/user/login".equals(uri) || "/user/register".equals(uri)) {
            return true;
        }

        String token = request.getHeader("token");
        if(token == null || token.isEmpty()){
            response.setStatus(401);
            return false;
        }
        // 下面是你的原有token校验代码
        try{
            jwtUtil.parseToken(token);
        }catch (Exception e){
            response.setStatus(401);
            return false;
        }
        return true;
    }

}

