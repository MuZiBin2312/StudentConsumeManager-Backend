package com.auggie.student_server.controller;

import com.auggie.student_server.service.OperationLogService;
import com.auggie.student_server.utils.ApiResponse;
import com.auggie.student_server.entity.OperationLog; // 导入新创建的封装类
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
            // 使用封装类中的参数调用服务层方法来记录日志
            operationLogService.addLog(logRequest.getModule(),
                    logRequest.getAction(),
                    logRequest.getOperator(),
                    logRequest.getRequestId(),
                    logRequest.getStatus());

            // 返回成功响应
            return ApiResponse.success(null);
        } catch (Exception e) {
            // 如果有异常，返回错误响应
            return ApiResponse.error(500, "日志记录失败: " + e.getMessage());
        }
    }
}