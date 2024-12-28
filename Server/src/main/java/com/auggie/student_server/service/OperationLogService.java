package com.auggie.student_server.service;

import com.auggie.student_server.entity.OperationLog;
import com.auggie.student_server.mapper.OperationLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperationLogService {

    @Autowired
    private OperationLogMapper operationLogMapper;

    // 添加操作日志
    public void addLog(String module, String action, String operator, String requestId, String status) {
        OperationLog operationLog = new OperationLog();
        operationLog.setModule(module);
        operationLog.setAction(action);
        operationLog.setOperator(operator);
        operationLog.setRequestId(requestId);
        operationLog.setStatus(status);
        operationLog.setTimestamp(new java.util.Date());  // 设置当前时间

        operationLogMapper.insertLog(operationLog);  // 调用 Mapper 执行插入操作
    }
}