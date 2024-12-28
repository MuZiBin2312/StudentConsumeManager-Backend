package com.auggie.student_server.controller;

import com.auggie.student_server.entity.OperationLog;
import com.auggie.student_server.service.OperationLogService;
import com.auggie.student_server.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/log")
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    // 新增操作日志
    @PostMapping("/add")
    public ApiResponse<Void> addLog(@RequestBody OperationLog logRequest) {
        try {
            operationLogService.addLog(logRequest.getModule(),
                    logRequest.getAction(),
                    logRequest.getOperator(),
                    logRequest.getRequestId(),
                    logRequest.getStatus());

            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(500, "日志记录失败: " + e.getMessage());
        }
    }

    // 查询所有日志记录
    @GetMapping("/all")
    public ApiResponse<List<OperationLog>> getAllLogs() {
        try {
            List<OperationLog> logs = operationLogService.findAllLogs();
            return ApiResponse.success(logs);
        } catch (Exception e) {
            return ApiResponse.error(500, "查询失败: " + e.getMessage());
        }
    }

    // 条件查询日志记录
    @PostMapping("/search")
    public ApiResponse<List<OperationLog>> searchLogs(@RequestBody Map<String, Object> params) {
        try {
            List<OperationLog> logs = operationLogService.findLogsByCriteria(params);
            return ApiResponse.success(logs);
        } catch (Exception e) {
            return ApiResponse.error(500, "条件查询失败: " + e.getMessage());
        }
    }
}