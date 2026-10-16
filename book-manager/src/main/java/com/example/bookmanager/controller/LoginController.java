package com.example.bookmanager.controller;

import com.example.bookmanager.common.Result;
import com.example.bookmanager.entity.User;
import com.example.bookmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> params, HttpServletRequest request) {
        String username = params.get("username");
        String password = params.get("password");

        if(username == null || username.isEmpty() || password == null || password.isEmpty()){
            return Result.fail("账号和密码不能为空");
        }

        User user = userService.login(username, password);
        // 重点！账号不存在 或者 密码错误，返回失败
        if(user == null){
            return Result.fail("账号或者密码错误");
        }
        request.getSession().setAttribute("loginUser", user);
        return Result.success(user);
    }

    // ============新增下面这两个接口============
    @GetMapping("/checkLogin")
    public Result checkLogin(HttpServletRequest request){
        Object loginUser = request.getSession().getAttribute("loginUser");
        if(loginUser == null){
            return Result.fail("未登录");
        }
        return Result.success(loginUser);
    }

    @PostMapping("/logout")
    public Result logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.invalidate(); //销毁session，退出登录
        return Result.success("退出成功");
    }

}
