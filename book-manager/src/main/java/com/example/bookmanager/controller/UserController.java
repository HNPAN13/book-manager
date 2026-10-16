package com.example.bookmanager.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookmanager.common.PageParam;
import com.example.bookmanager.common.Result;
import com.example.bookmanager.entity.User;
import com.example.bookmanager.service.UserService;
import com.example.bookmanager.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;


    /**
     * 用户分页列表 GET /user/list?current=1&size=5
     */
    @GetMapping("/list")
    public Result<Page<User>> list(PageParam param){
        Page<User> page = new Page<>(param.getCurrent(), param.getSize());
        Page<User> pageData = userService.page(page, new LambdaQueryWrapper<>());
        return Result.success(pageData);
    }

    /**
     * 根据id查询用户 GET /user/{id}
     */
    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id){
        User user = userService.getById(id);
        if(user == null){
            return Result.fail("用户不存在");
        }
        return Result.success(user);
    }

    /**
     * 管理员新增用户 POST /user/add
     */
    @PostMapping("/add")
    public Result<String> add(@RequestBody User user){
        if(user.getUsername() == null || user.getUsername().trim().isEmpty()){
            return Result.fail("账号不能为空");
        }
        if(user.getPassword() == null || user.getPassword().trim().isEmpty()){
            return Result.fail("密码不能为空");
        }
        boolean ok = userService.save(user);
        return ok ? Result.success("新增用户成功") : Result.fail("新增失败");
    }

    /**
     * 管理员修改用户 PUT /user/update
     */
    @PutMapping("/update")
    public Result<String> update(@RequestBody User user){
        if(user.getId() == null){
            return Result.fail("用户id不能为空");
        }
        boolean ok = userService.updateById(user);
        return ok ? Result.success("修改成功") : Result.fail("修改失败");
    }

    /**
     * 管理员删除用户 DELETE /user/delete/{id}
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id){
        boolean ok = userService.removeById(id);
        return ok ? Result.success("删除成功") : Result.fail("删除失败");
    }

    /**
     * 用户登录 POST /user/login
     */
    @PostMapping("/login")
    public Result<Map<String,Object>> login(@RequestParam String username,
                                            @RequestParam String password){
        User user = userService.login(username, password);
        if(user == null){
            return Result.fail("账号或者密码错误");
        }
        //静态调用Jwt工具生成token
        String token = jwtUtil.createToken(user.getId(), user.getUsername());
        Map<String,Object> data = new HashMap<>();
        data.put("token",token);
        data.put("userId",user.getId());
        data.put("username",user.getUsername());
        return Result.success(data);
    }

    /**
     * 用户注册 POST /user/register
     */
    @PostMapping("/register")
    public Result<?> register(@RequestBody User user){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,user.getUsername());
        User existUser = userService.getOne(wrapper);
        if(existUser != null){
            return Result.fail("账号已存在！");
        }
        //密码加密，你项目要有BCrypt依赖
        user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt()));
        user.setRole("user");
        boolean save = userService.save(user);
        if(save){
            return Result.success("注册成功");
        }else{
            return Result.fail("注册失败");
        }
    }


    /**
     * 修改密码 POST /user/updatePwd
     */
    @PostMapping("/updatePwd")
    public Result<String> updatePwd(@RequestParam Long userId,
                                    @RequestParam String oldPwd,
                                    @RequestParam String newPwd){
        String res = userService.updatePwd(userId,oldPwd,newPwd);
        return Result.success(res);
    }

}
