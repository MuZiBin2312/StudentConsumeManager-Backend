package com.auggie.student_server.controller;

import com.auggie.student_server.entity.OperationLog;
import com.auggie.student_server.service.OperationLogService;
import com.auggie.student_server.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/log")
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private HttpServletRequest request; // 注入 HttpServletRequest

    // 新增操作日志
    @PostMapping("/add")
    public ApiResponse<Void> addLog(@RequestBody OperationLog logRequest) {
        try {
            // 从请求中解析 host 信息（IP 和端口）
            String host = getHostFromRequest();

            // 假设从请求体中获取 extraInfo
            String extraInfo = logRequest.getStatus(); // 你可以根据需求修改这一部分

            // 调用 Service 层的 addLog 方法，传递 7 个参数
            operationLogService.addLog(
                    logRequest.getModule(),
                    logRequest.getAction(),
                    logRequest.getOperator(),
                    logRequest.getRequestId(),
                    logRequest.getStatus(),
                    host, // 传递解析后的 host
                    extraInfo // 传递额外的信息
            );

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

    // 从请求中解析 host 信息
    private String getHostFromRequest() {
        String ip = request.getRemoteAddr(); // 获取客户端 IP
        int port = request.getRemotePort();  // 获取客户端端口
        return ip + ":" + port;              // 组合成 host 信息
    }
}
