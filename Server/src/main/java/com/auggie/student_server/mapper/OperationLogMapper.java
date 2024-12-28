package com.auggie.student_server.mapper;

import com.auggie.student_server.entity.OperationLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperationLogMapper {

    // 插入日志记录，SQL 语句将放在 XML 文件中
    void insertLog(OperationLog operationLog);
}