package com.example.bookmanager.service.impl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookmanager.entity.User;
import com.example.bookmanager.mapper.UserMapper;
import com.example.bookmanager.service.UserService;
import com.example.bookmanager.util.BCryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder encoder;


    @Override
    public String register(String username, String password, String realName, String phone) {
        // 1. 判断用户名是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User exist = userMapper.selectOne(wrapper);
        if(exist != null){
            return "用户名已被占用";
        }

        // 2. 密码加密
        String encodePwd = BCryptUtil.encode(password);

        // 3. 组装用户：注册默认普通用户 role = user
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodePwd);
//        user.setRealName(realName);
//        user.setPhone(phone);
        user.setRole("user");

        userMapper.insert(user);
        return "注册成功";
    }

    @Override
    public String updatePwd(Long userId, String oldPwd, String newPwd) {
        // 1. 查询本人
        User user = userMapper.selectById(userId);
        if(user == null){
            return "用户不存在";
        }

        // 2. 校验旧密码
        if(!BCryptUtil.matches(oldPwd, user.getPassword())){
            return "旧密码不正确";
        }

        // 3. 简单校验新密码长度
        if(newPwd.length() < 4){
            return "新密码不能少于4位";
        }

        // 4. 设置新加密密码
        user.setPassword(BCryptUtil.encode(newPwd));
        userMapper.updateById(user);

        return "密码修改成功，请重新登录";
    }

    @Override
    public User login(String username, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(wrapper);

        if (user == null) {
            return null;
        }

        System.out.println("输入密码：" + password);
        System.out.println("库中密文：" + user.getPassword());
        boolean ok = encoder.matches(password, user.getPassword());
        System.out.println("校验：" + ok);

        if (!ok) {
            return null;
        }
        return user;
    }
}
