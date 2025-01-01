package com.auggie.student_server.service;

import com.auggie.student_server.entity.TradeRecord;
import com.auggie.student_server.mapper.TradeRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeRecordService {

    @Autowired
    private TradeRecordMapper tradeRecordMapper;

    public List<TradeRecord> getRecordsByTimeRange(String startTime, String endTime) {
        return tradeRecordMapper.findByTimeRange(startTime, endTime);
    }
    // 获取所有记录
    public List<TradeRecord> getAllRecords() {
        return tradeRecordMapper.findAll();
    }
}
