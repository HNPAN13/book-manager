package com.example.bookmanager.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
// SpringBoot3 就把上面 javax 改成 jakarta

public class BorrowDTO {

    @NotNull(message = "图书ID不能为空")
    @Min(value = 1, message = "图书ID必须大于0")
    private Integer bookId;

    @NotNull(message = "用户ID不能为空")
    @Min(value = 1, message = "用户ID必须大于0")
    private Integer userId;

    // getter / setter
    public Integer getBookId() { return bookId; }
    public void setBookId(Integer bookId) { this.bookId = bookId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
}
