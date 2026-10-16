package com.example.bookmanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

// 功能：告诉MP这个类对应哪张数据库表
@TableName("book")
public class Book {

    // @TableId：标记这是主键；type自增和数据库 AUTO_INCREMENT 对应
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String bookName;
    private String author;
    private Integer stock;
    private LocalDateTime createTime;

    // 下面：getter / setter（用来取、赋值）+ 空构造
    public Book() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
