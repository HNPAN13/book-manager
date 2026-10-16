package com.example.bookmanager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bookmanager.entity.Book;

public interface BookService extends IService<Book> {


    /**
     * 删除图书，校验是否存在未归还
     * @param bookId 图书id
     * @return 提示信息
     */
    String deleteBookCheckBorrow(Long bookId, String operatorRole);
    // 新增、修改也加上（按需，这里我们规定：只有管理员能增删改图书）
    String addBook(Book book, String operatorRole);
    String updateBook(Book book, String operatorRole);




}
