package com.example.bookmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookmanager.common.Result;
import com.example.bookmanager.entity.Book;
import com.example.bookmanager.entity.BorrowRecord;
import com.example.bookmanager.exception.BusinessException;
import com.example.bookmanager.mapper.BorrowRecordMapper;
import com.example.bookmanager.service.BookService;
import com.example.bookmanager.service.BorrowRecordService;
import com.example.bookmanager.vo.BorrowVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BorrowRecordServiceImpl extends ServiceImpl<BorrowRecordMapper, BorrowRecord> implements BorrowRecordService {

    @Resource
    private BookService bookService;

    @Override
    public List<BorrowRecord> getByUserId(Long userId) {
        LambdaQueryWrapper<BorrowRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BorrowRecord::getUserId, userId);
        wrapper.orderByDesc(BorrowRecord::getBorrowTime);
        return this.list(wrapper);
    }

    @Override
    public List<BorrowRecord> getUnReturnByUserId(Long userId) {
        LambdaQueryWrapper<BorrowRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BorrowRecord::getUserId, userId)
                .eq(BorrowRecord::getIsReturn, 0); // 修改：使用实体字段 isReturn
        wrapper.orderByDesc(BorrowRecord::getBorrowTime);
        return this.list(wrapper);
    }

    @Override
    public List<BorrowRecord> getUserBorrowRecord(Long userId) {
        LambdaQueryWrapper<BorrowRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BorrowRecord::getUserId, userId);
        wrapper.orderByDesc(BorrowRecord::getBorrowTime);
        return this.list(wrapper);
    }

    /**
     * 借书：新增借阅记录，图书库存-1
     */
    @Override
    @Transactional
    public void borrowBook(Long bookId, Long userId) {
        // 查询图书
        Book book = bookService.getById(bookId);
        if (book == null) {
            throw new BusinessException("图书不存在");
        }
        // 库存校验
        if (book.getStock() <= 0) {
            throw new BusinessException("该书库存不足，无法借阅");
        }

        // =========核心新增：校验用户是否已经借了这本且未归还=========
        LambdaQueryWrapper<BorrowRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BorrowRecord::getBookId, bookId)
                .eq(BorrowRecord::getUserId, userId)
                .eq(BorrowRecord::getIsReturn, 0); //0=未归还
        long existCount = this.count(wrapper);
        if (existCount > 0) {
            throw new BusinessException("你已借阅本书尚未归还，不能重复借书");
        }

        // 库存-1
        book.setStock(book.getStock() - 1);
        bookService.updateById(book);

        // 新增借阅记录
        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setBookId(bookId);
        borrowRecord.setUserId(userId);
        borrowRecord.setIsReturn(0);
        borrowRecord.setBorrowTime(LocalDateTime.now());
        this.save(borrowRecord);
    }


    /**
     * 还书：修改状态、归还时间，图书库存+1
     */
    @Override
    @Transactional
    public Result<String> returnBook(Long recordId) {
        BorrowRecord record = getById(recordId);
        if(record == null){
            return Result.fail("借阅记录不存在");
        }
        if(record.getIsReturn() == 1){
            return Result.fail("该书已经归还");
        }
        // 修改借阅记录状态
        record.setIsReturn(1);
        record.setReturnTime(LocalDateTime.now());
        this.updateById(record);

        // 库存+1
        Book book = bookService.getById(record.getBookId());
        book.setStock(book.getStock() + 1);
        bookService.updateById(book);

        return Result.success("还书成功");
    }


    @Resource
    private BorrowRecordMapper borrowRecordMapper;

    @Override
    public IPage<BorrowVO> getBorrowVOPage(Page<BorrowVO> page) {
        return borrowRecordMapper.selectBorrowPage(page);
    }
}
