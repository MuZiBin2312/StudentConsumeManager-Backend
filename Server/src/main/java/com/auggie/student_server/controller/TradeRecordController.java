package com.auggie.student_server.controller;

import com.auggie.student_server.entity.TradeRecord;
import com.auggie.student_server.service.TradeRecordService;
import com.auggie.student_server.utils.ApiResponse;
import com.auggie.student_server.utils.FieldNameMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/trade")
public class TradeRecordController {

    @Autowired
    private TradeRecordService tradeRecordService;

    @PostMapping("/search")
    public ApiResponse<List<Map<String, Object>>> getByTimeRange(@RequestBody Map<String, String> timeRange) {
        String startTime = timeRange.get("startTime");
        String endTime = timeRange.get("endTime");

        List<TradeRecord> records;

        // 判断是否有时间条件
        if (startTime == null || endTime == null) {
            // 无时间条件，返回全部记录
            records = tradeRecordService.getAllRecords();
        } else {
            // 有时间条件，按时间范围查询
            records = tradeRecordService.getRecordsByTimeRange(startTime, endTime);
        }

        // 转换字段名为中文
        List<Map<String, Object>> chineseFieldRecords = convertToChineseFieldNames(records);

        return ApiResponse.success(chineseFieldRecords);
    }

    // 将字段名转换为中文
    private List<Map<String, Object>> convertToChineseFieldNames(List<TradeRecord> tradeRecords) {
        Map<String, String> fieldMap = FieldNameMapper.getFieldMap();

        // 将每条记录的字段名转换成中文
        return tradeRecords.stream().map(record -> {
            Map<String, Object> chineseRecord = new HashMap<>();
            chineseRecord.put(fieldMap.get("id"), record.getId());
            chineseRecord.put(fieldMap.get("monthYear"), record.getMonthYear());
            chineseRecord.put(fieldMap.get("cateringCurrentValue"), record.getCateringCurrentValue());
            chineseRecord.put(fieldMap.get("cateringCumulativeValue"), record.getCateringCumulativeValue());
            chineseRecord.put(fieldMap.get("cateringGrowthRate"), record.getCateringGrowthRate());
            chineseRecord.put(fieldMap.get("cateringCumulativeGrowth"), record.getCateringCumulativeGrowth());
            chineseRecord.put(fieldMap.get("cateringAboveCurrentValue"), record.getCateringAboveCurrentValue());
            chineseRecord.put(fieldMap.get("cateringAboveCumulativeValue"), record.getCateringAboveCumulativeValue());
            chineseRecord.put(fieldMap.get("cateringAboveGrowthRate"), record.getCateringAboveGrowthRate());
            chineseRecord.put(fieldMap.get("cateringAboveCumulativeGrowth"), record.getCateringAboveCumulativeGrowth());
            chineseRecord.put(fieldMap.get("retailCurrentValue"), record.getRetailCurrentValue());
            chineseRecord.put(fieldMap.get("retailCumulativeValue"), record.getRetailCumulativeValue());
            chineseRecord.put(fieldMap.get("retailGrowthRate"), record.getRetailGrowthRate());
            chineseRecord.put(fieldMap.get("retailCumulativeGrowth"), record.getRetailCumulativeGrowth());
            chineseRecord.put(fieldMap.get("retailAboveCurrentValue"), record.getRetailAboveCurrentValue());
            chineseRecord.put(fieldMap.get("retailAboveCumulativeValue"), record.getRetailAboveCumulativeValue());
            chineseRecord.put(fieldMap.get("retailAboveGrowthRate"), record.getRetailAboveGrowthRate());
            chineseRecord.put(fieldMap.get("retailAboveCumulativeGrowth"), record.getRetailAboveCumulativeGrowth());
            return chineseRecord;
        }).collect(Collectors.toList());
    }
}