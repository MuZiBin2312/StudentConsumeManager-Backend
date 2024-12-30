package com.auggie.student_server.controller;

import com.auggie.student_server.entity.ConsumeRecord;
import com.auggie.student_server.service.RecordService;
import com.auggie.student_server.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ApiResponse<Boolean> addRecord(@RequestBody ConsumeRecord record) {
        try {
            System.out.println("新增消费记录: " + record);
            boolean result = recordService.addRecord(record);

            // 根据业务逻辑判断返回结果
            if (result) {
                return ApiResponse.success(true); // 业务成功
            } else {
                return ApiResponse.businessError("新增消费记录失败"); // 业务失败
            }
        } catch (Exception e) {
            // 系统错误返回 HTTP 500 状态
            return ApiResponse.error(500, "服务器内部错误: " + e.getMessage());
        }
    }

    // 获取所有消费记录
    @GetMapping("/all")
    public ApiResponse<List<ConsumeRecord>> getAllRecords() {
        try {
            List<ConsumeRecord> records = recordService.getAllRecords();
            return ApiResponse.success(records);
        } catch (Exception e) {
            return ApiResponse.error(500, "服务器内部错误: " + e.getMessage());
        }
    }

    // 根据查询条件获取消费记录
    @PostMapping("/search")
    public ApiResponse<List<ConsumeRecord>> findByCriteria(@RequestBody Map<String, Object> criteria) {
        try {
            List<ConsumeRecord> records = recordService.findByCriteria(criteria);
            return ApiResponse.success(records);
        } catch (Exception e) {
            return ApiResponse.error(500, "服务器内部错误: " + e.getMessage());
        }
    }

    // 根据记录ID查询消费记录
    @GetMapping("/findById/{recordId}")
    public ApiResponse<ConsumeRecord> findById(@PathVariable("recordId") Integer recordId) {
        try {
            System.out.println("查询消费记录 By ID: " + recordId);
            ConsumeRecord record = recordService.findById(recordId);
            if (record != null) {
                return ApiResponse.success(record);
            } else {
                return ApiResponse.businessError("记录未找到");
            }
        } catch (Exception e) {
            return ApiResponse.error(500, "服务器内部错误: " + e.getMessage());
        }
    }

    // 根据ID删除消费记录
    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> deleteRecordById(@PathVariable("id") int id) {
        try {
            boolean success = recordService.deleteRecordById(id);
            if (success) {
                return ApiResponse.success("记录删除成功");
            } else {
                return ApiResponse.businessError("记录未找到");
            }
        } catch (Exception e) {
            return ApiResponse.error(500, "服务器内部错误: " + e.getMessage());
        }
    }

    // 根据ID更新消费记录
    @PutMapping("/{id}")
    public ApiResponse<String> updateRecord(@PathVariable Integer id, @RequestBody ConsumeRecord record) {
        try {
            record.setRecordId(id);
            int updatedRows = recordService.updateRecordById(record);
            if (updatedRows > 0) {
                return ApiResponse.success("记录更新成功");
            } else {
                return ApiResponse.businessError("记录未找到");
            }
        } catch (Exception e) {
            return ApiResponse.error(500, "服务器内部错误: " + e.getMessage());
        }
    }


    // 批量删除消费记录
    @DeleteMapping("/batchDelete")
    public ApiResponse<String> batchDelete(@RequestBody List<Integer> ids) {
        try {
            boolean success = recordService.batchDelete(ids);
            if (success) {
                return ApiResponse.success("批量删除成功");
            } else {
                return ApiResponse.businessError("未找到对应的记录");
            }
        } catch (Exception e) {
            return ApiResponse.error(500, "服务器内部错误: " + e.getMessage());
        }
    }

    // 测试日志接口
    @GetMapping("/log")
    public ApiResponse<Integer> log() {
        System.out.println("RecordController log");
        return ApiResponse.success(1);
    }
}