package com.example.bookmanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookmanager.entity.BorrowRecord;
import com.example.bookmanager.vo.BorrowVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BorrowRecordMapper extends BaseMapper<BorrowRecord> {
    // 新增联表分页方法
    IPage<BorrowVO> selectBorrowPage(Page<BorrowVO> page);
}
