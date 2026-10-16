package com.example.bookmanager.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BorrowVO {
    private Long id;
    private Long bookId;
    private String bookName;
    private Long userId;
    private String username;
    private LocalDateTime borrowTime;
    private LocalDateTime returnTime;
    private Integer isReturn;
}
