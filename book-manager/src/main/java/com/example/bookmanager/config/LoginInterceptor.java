package com.example.bookmanager.config;

import com.example.bookmanager.common.Result;
import com.example.bookmanager.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        // 放行OPTIONS预检请求，否则跨域直接失效
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String uri = request.getRequestURI();
        // 放行登录、登出
        if ("/login".equals(uri) || "/login/".equals(uri) || "/logout".equals(uri)) {
            return true;
        }

        // ✅ 重点！加上 false：没有session就返回null，不要自动创建空session！
        HttpSession session = request.getSession(false);
        User loginUser = null;
        if(session != null){
            loginUser = (User) session.getAttribute("loginUser");
        }

        if (loginUser == null) {
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Result<?> res = Result.build(401,"未登录，请先登录",null);
            objectMapper.writeValue(response.getWriter(), res);
            return false;
        }
        return true;
    }
}
