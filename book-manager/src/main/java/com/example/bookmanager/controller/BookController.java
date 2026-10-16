package com.example.bookmanager.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookmanager.common.Result;
import com.example.bookmanager.entity.Book;
import com.example.bookmanager.entity.BorrowRecord;
import com.example.bookmanager.exception.BusinessException;
import com.example.bookmanager.service.BookService;
import com.example.bookmanager.service.BorrowRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/book")
@CrossOrigin(origins ="*")
public class BookController {

    @Autowired
    private BookService bookService;
    @Autowired
    private BorrowRecordService borrowRecordService;

    // 1. 分页列表（支持按书名模糊搜索） GET /book/list
// 1. 分页列表（支持按书名/作者模糊搜索） GET /book/list
    @GetMapping("/list")
    public Result<Page<Book>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            String key
    ){
        Page<Book> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();

        // 模糊查询：书名 或者 作者包含关键词
        if(key != null && !key.trim().isEmpty()){
            wrapper.like(Book::getBookName, key).or()
                    .like(Book::getAuthor, key);
        }
        wrapper.orderByDesc(Book::getId); //id倒序，最新新增的在最上面

        Page<Book> pageData = bookService.page(page, wrapper);
        return Result.success(pageData);
    }


    /**
     * 新增图书
     */
    @PostMapping("/add")
    public Result<String> add(@RequestBody Book book, HttpServletRequest request){
        //    String currentRole = (String) request.getAttribute("currentRole");
        //    if(!"admin".equals(currentRole)){
        //        return Result.fail("权限不足，只有管理员可以新增图书");
        //    }

        // =====新增参数校验=====
        // 书名非空
        if(book.getBookName() == null || book.getBookName().trim().equals("")){
            return Result.fail("书名不能为空");
        }
        // 作者非空
        if(book.getAuthor() == null || book.getAuthor().trim().equals("")){
            return Result.fail("作者不能为空");
        }
        // 库存校验：不能小于0
        if(book.getStock() == null || book.getStock() < 0){
            return Result.fail("库存不能为负数");
        }

        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Book::getBookName, book.getBookName().trim())
                .eq(Book::getAuthor, book.getAuthor().trim());
        Book existBook = bookService.getOne(wrapper, false);

        if(existBook != null){
            // 找到相同图书：库存相加
            existBook.setStock(existBook.getStock() + book.getStock());
            bookService.updateById(existBook);
            return Result.success("图书库存已累加成功");
        }else{
            // 存之前把前后空格去掉
            book.setBookName(book.getBookName().trim());
            book.setAuthor(book.getAuthor().trim());
            book.setCreateTime(LocalDateTime.now());
            boolean ok = bookService.save(book);
            System.out.println("save执行结果 ok="+ok);
            return ok ? Result.success("图书新增成功") : Result.fail("新增失败");
        }
    }

    /**
     * 修改图书
     */
    @PutMapping("/update")
    public Result<String> update(@RequestBody Book book, HttpServletRequest request) {
        //String operatorRole = (String) request.getAttribute("currentRole");
        //String resMsg = bookService.updateBook(book, operatorRole);
        String resMsg = bookService.updateBook(book, "admin");

        if ("修改成功".equals(resMsg)) {
            return Result.success("图书修改成功");
        } else {
            return Result.fail(resMsg);
        }
    }

    /**
     * 删除图书
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id,
                                 @RequestParam String operatorRole) {
        try {
            String msg = bookService.deleteBookCheckBorrow(id, operatorRole);
            return Result.success(msg);
        } catch (BusinessException e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 根据id查询单本图书 GET /book/get/{id}
     */
    @GetMapping("/get/{id}")
    public Result<Book> getOneBook(@PathVariable Integer id){
        Book book = bookService.getById(id);
        if(book == null){
            return Result.fail("图书不存在");
        }
        return Result.success(book);
    }


    //借书接口【手动参数校验，使用Query Params传参】
    @PostMapping("/borrowBook")
    public Result<String> borrowBook(
            @RequestParam("bookId") Long bookId,
            @RequestParam("userId") Long userId
    ){
        if(bookId == null || bookId < 1){
            return Result.fail("图书ID必须大于0");
        }
        if(userId == null || userId < 1){
            return Result.fail("用户ID必须大于0");
        }
        // ✔改为调用 borrowRecordService
        borrowRecordService.borrowBook(bookId, userId);
        return Result.success("借书成功");
    }


    /**
     * 查询某个用户所有借阅（含已还、未还）
     * GET /book/myRecord?userId=1
     */
    @GetMapping("/myRecord")
    public Result<?> myRecord(Long userId){
        if(userId == null || userId < 1){
            return Result.fail("用户ID必须大于0");
        }
        LambdaQueryWrapper<BorrowRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BorrowRecord::getUserId, userId)
                .orderByDesc(BorrowRecord::getBorrowTime);

        List<BorrowRecord> list = borrowRecordService.list(wrapper);
        return Result.success(list);
    }

    /**
     * 根据用户id查询该用户全部借阅记录
     * GET /borrow/list/{userId}
     */
    @GetMapping("/borrow/list/{userId}")
    public Result<List<BorrowRecord>> getUserBorrowList(@PathVariable Long userId){
        List<BorrowRecord> list = borrowRecordService.getByUserId(userId);
        return Result.success(list);
    }

    /**
     * 获取用户当前未还的书
     * GET /book/borrow/unreturn/{userId}
     */
    @GetMapping("/borrow/unreturn/{userId}")
    public Result<List<BorrowRecord>> getUnReturnList(@PathVariable Long userId){
        return Result.success(borrowRecordService.getUnReturnByUserId(userId));
    }

}
