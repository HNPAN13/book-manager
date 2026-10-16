package com.example.bookmanager.controller;

import com.example.bookmanager.entity.Book;
import com.example.bookmanager.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private BookMapper bookMapper;

    @GetMapping("/db")
    public Book testDb(){
        return bookMapper.selectById(1);
    }
}
