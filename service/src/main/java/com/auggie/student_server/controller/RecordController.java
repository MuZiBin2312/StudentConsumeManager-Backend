package com.auggie.student_server.controller;

import com.auggie.student_server.entity.ConsumeRecord;
import com.auggie.student_server.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Auther: auggie
 * @Date: 2024/12/27
 * @Description: RecordController
 * @Version 1.0.0
 */

@RestController
@CrossOrigin("*")
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    // 新增消费记录
    @PostMapping("/addRecord")
    public boolean addRecord(@RequestBody ConsumeRecord record) {
        System.out.println("新增消费记录: " + record);
        return recordService.addRecord(record);
    }

    @GetMapping("/all")
    public List<ConsumeRecord> getAllRecords() {
        return recordService.getAllRecords();
    }

    @PostMapping("/search")
    public List<ConsumeRecord> findByCriteria(@RequestBody Map<String, Object> criteria) {
        return recordService.findByCriteria(criteria);
    }

    // 根据记录ID查询消费记录
    @GetMapping("/findById/{recordId}")
    public ConsumeRecord findById(@PathVariable("recordId") Integer recordId) {
        System.out.println("查询消费记录 By ID: " + recordId);
        return recordService.findById(recordId);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRecordById(@PathVariable("id") int id) {
        boolean success = recordService.deleteRecordById(id);
        if (success) {
            return ResponseEntity.ok("Record deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found");
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateRecord(@PathVariable Integer id, @RequestBody ConsumeRecord record) {
        record.setRecordId(id);
        int updatedRows = recordService.updateRecordById(record);

        if (updatedRows > 0) {
            return ResponseEntity.ok("Record updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found");
        }
    }

    // 条件查询消费记录
//    @PostMapping("/findBySearch")
//    public List<ConsumeRcrd> findBySearch(@RequestBody Map<String, String> map) {
//        System.out.println("条件查询消费记录: " + map);
//        return recordService.findBySearch(map);
//    }

    // 删除消费记录
//    @GetMapping("/deleteById/{recordId}")
//    public boolean deleteById(@PathVariable("recordId") String recordId) {
//        System.out.println("删除消费记录 By ID: " + recordId);
//        return recordService.deleteById(recordId);
//    }

    // 更新消费记录
//    @PostMapping("/updateRecord")
//    public boolean updateRecord(@RequestBody ConsumeRecord record) {
//        System.out.println("更新消费记录: " + record);
//        return recordService.updateRecord(record);
//    }

    // 测试日志接口
    @GetMapping("/log")
    public int log() {
        System.out.println("RecordController log");
        return 1;
    }
}