package com.example.bookmanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bookmanager.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
