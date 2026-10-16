package com.example.bookmanager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bookmanager.common.Result;
import com.example.bookmanager.entity.BorrowRecord;
import com.example.bookmanager.vo.BorrowVO;

import java.util.List;

public interface BorrowRecordService extends IService<BorrowRecord> {
    List<BorrowRecord> getByUserId(Long userId);
    List<BorrowRecord> getUnReturnByUserId(Long userId);
    List<BorrowRecord> getUserBorrowRecord(Long userId);

    void borrowBook(Long bookId, Long userId);
    Result<String> returnBook(Long recordId);

    // ✅只新增这一行，其余原有代码全部保留
    IPage<BorrowVO> getBorrowVOPage(Page<BorrowVO> page);

}
