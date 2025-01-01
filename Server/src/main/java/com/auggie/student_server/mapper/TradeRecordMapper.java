package com.auggie.student_server.mapper;


import com.auggie.student_server.entity.TradeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface TradeRecordMapper {
    // 根据时间范围筛选记录
    List<TradeRecord> findByTimeRange(
            @Param("startTime") String startTime,
            @Param("endTime") String endTime
    );
    // 查询所有记录
    List<TradeRecord> findAll();
}
