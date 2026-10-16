package com.example.bookmanager.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookmanager.common.Result;
import com.example.bookmanager.entity.BorrowRecord;
import com.example.bookmanager.service.BorrowRecordService;
import com.example.bookmanager.vo.BorrowVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/borrow")
public class BorrowRecordController {

    @Resource
    private BorrowRecordService borrowRecordService;

    // =========替换后的新联表分页接口=========
    @GetMapping("/list")
    public Result<Page<BorrowVO>> list(Integer current, Integer size) {
        if(current == null) current = 1;
        if(size == null) size = 10;
        Page<BorrowVO> page = new Page<>(current, size);
        Page<BorrowVO> pageData = (Page<BorrowVO>) borrowRecordService.getBorrowVOPage(page);
        return Result.success(pageData);
    }


    // 根据id查询单条记录 GET /borrow/1
    @GetMapping("/{id}")
    public Result<BorrowRecord> getOne(@PathVariable Long id) {
        BorrowRecord record = borrowRecordService.getById(id);
        return Result.success(record);
    }

    // 新增借阅记录
    @PostMapping
    public Result<Boolean> add(@RequestBody BorrowRecord borrowRecord) {
        boolean ok = borrowRecordService.save(borrowRecord);
        return Result.success(ok);
    }

    // 修改借阅记录
    @PutMapping
    public Result<Boolean> update(@RequestBody BorrowRecord borrowRecord) {
        boolean ok = borrowRecordService.updateById(borrowRecord);
        return Result.success(ok);
    }

    // 删除借阅记录
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean ok = borrowRecordService.removeById(id);
        return Result.success(ok);
    }

    // 【新增】根据用户ID查询该用户所有借阅记录 GET /borrow/user/1
    @GetMapping("/user/{userId}")
    public Result<List<BorrowRecord>> getUserRecord(@PathVariable Long userId){
        List<BorrowRecord> list = borrowRecordService.getUserBorrowRecord(userId);
        return Result.success(list);
    }
    @PostMapping("/returnBook")
    public Result<String> returnBook(@RequestParam Long recordId) {
        return borrowRecordService.returnBook(recordId);
    }


}
