package com.auggie.student_server.service;

import com.auggie.student_server.entity.ConsumeRecord;
import com.auggie.student_server.mapper.RecordMapper;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;


@Service
public class RecordService {

    @Autowired
    private RecordMapper recordMapper;

    public boolean addRecord(ConsumeRecord consumeRecord) {
        return recordMapper.addRecord(consumeRecord); // 使用 record 参数
    }

    public boolean deleteRecordById(int recordId) {
        return recordMapper.deleteById(recordId) > 0;
    }

    public List<ConsumeRecord> getAllRecords() {
        return recordMapper.selectAllRecords();
    }


    public List<ConsumeRecord> findByCriteria(Map<String, Object> criteria) {
        return recordMapper.findByCriteria(criteria);
    }


    public ConsumeRecord findById(Integer recordId) {
        return recordMapper.findById(recordId);
    }


    public int updateRecordById(ConsumeRecord record) {
        return recordMapper.updateRecordById(record);
    }


    public boolean batchDelete(List<Integer> recordIds) {
        return recordMapper.batchDelete(recordIds) > 0;
    }

}
