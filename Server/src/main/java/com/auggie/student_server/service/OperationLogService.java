package com.auggie.student_server.service;

import com.auggie.student_server.entity.OperationLog;
import com.auggie.student_server.mapper.OperationLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OperationLogService {

    @Autowired
    private OperationLogMapper operationLogMapper;

    // 添加操作日志
    public void addLog(String module, String action, String operator, String requestId, String status, String host, String errorMessage) {
        OperationLog operationLog = new OperationLog();
        operationLog.setModule(module);      // 设置模块名称
        operationLog.setAction(action);      // 设置操作类型
        operationLog.setOperator(operator);  // 设置操作人
        operationLog.setRequestId(requestId);// 设置请求ID
        operationLog.setStatus(status);      // 设置状态
        operationLog.setTimestamp(new java.util.Date()); // 设置当前时间
        operationLog.setHost(host);          // 设置客户端IP和端口信息
        operationLog.setErrorMessage(errorMessage); // 设置错误信息

        operationLogMapper.insertLog(operationLog); // 调用 Mapper 执行插入操作
    }

    // 查询所有日志记录
    public List<OperationLog> findAllLogs() {
        return operationLogMapper.findAll();
    }

    // 条件查询日志记录
    public List<OperationLog> findLogsByCriteria(Map<String, Object> params) {
        return operationLogMapper.findByCriteria(params);
    }
}
