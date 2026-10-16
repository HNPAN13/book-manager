package com.example.bookmanager.common;

import lombok.Data;

@Data
public class PageParam {
    private Long current = 1L;   // 当前页
    private Long size = 10L;     // 每页条数
}
