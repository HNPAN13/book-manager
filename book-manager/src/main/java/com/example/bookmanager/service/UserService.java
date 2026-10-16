package com.example.bookmanager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bookmanager.entity.User;

public interface UserService extends IService<User> {

    /**
     * 登录校验
     * @param username 账号
     * @param password 密码
     * @return 用户对象
     */
    User login(String username,String password);

    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @param realName 真实姓名
     * @param phone 手机号
     * @return 返回提示信息
     */
    String register(String username, String password, String realName, String phone);

    /**
     * 修改密码
     * @param userId 用户id
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @return 返回提示信息
     */
    String updatePwd(Long userId, String oldPwd, String newPwd);
}
