package com.example.bookmanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bookmanager.entity.Book;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookMapper extends BaseMapper<Book> {
    // 继承BaseMapper，就拥有了基础CRUD方法，这里暂时不用写任何代码
}
