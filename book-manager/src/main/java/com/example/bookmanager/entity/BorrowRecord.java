package com.example.bookmanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("borrow_record")
public class BorrowRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long bookId;
    private Long userId;

    private LocalDateTime borrowTime;
    private LocalDateTime returnTime;

    // 0未归还，1已归还，字段名叫isReturn，数据库is_return
    private Integer isReturn;
}
