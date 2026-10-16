package com.example.bookmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookmanager.entity.Book;
import com.example.bookmanager.entity.BorrowRecord;
import com.example.bookmanager.exception.BusinessException;
import com.example.bookmanager.mapper.BookMapper;
import com.example.bookmanager.mapper.BorrowRecordMapper;
import com.example.bookmanager.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book>
        implements BookService {

    @Resource
    private BorrowRecordMapper borrowRecordMapper;

    // 删除图书，校验是否存在未归还借阅
    @Override
    @Transactional
    public String deleteBookCheckBorrow(Long bookId, String operatorRole) {
        // 权限校验：只有管理员可删除
        if (!"admin".equals(operatorRole)) {
            throw new BusinessException("权限不足：只有管理员可以删除图书");
        }

        Book book = this.getById(bookId);
        if (book == null) {
            throw new BusinessException("图书不存在");
        }

        LambdaQueryWrapper<BorrowRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BorrowRecord::getBookId, bookId)
                .eq(BorrowRecord::getIsReturn, 0);
        // 使用mapper查询数量，不再调用BorrowRecordService
        long count = borrowRecordMapper.selectCount(wrapper);

        if (count > 0) {
            throw new BusinessException("操作失败：该图书还有未归还的借阅记录，不能删除");
        }

        this.removeById(bookId);
        return "删除成功";
    }

    // 新增图书，权限校验
    @Override
    @Transactional
    public String addBook(Book book, String operatorRole) {
        if (!"admin".equals(operatorRole)) {
            throw new BusinessException("权限不足：只有管理员可以新增图书");
        }
        this.save(book);
        return "新增成功";
    }

    // 修改图书，权限校验
    @Override
    @Transactional
    public String updateBook(Book book, String operatorRole) {
        if (!"admin".equals(operatorRole)) {
            throw new BusinessException("权限不足，只有管理员可以修改图书");
        }
        if(book.getId() == null){
            throw new BusinessException("图书id不能为空，无法执行修改");
        }
        boolean success = this.updateById(book);
        System.out.println("update执行结果：" + success);
        if(!success){
            throw new BusinessException("修改失败，找不到该图书");
        }
        return "修改成功";
    }

}
