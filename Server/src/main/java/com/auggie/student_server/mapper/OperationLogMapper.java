package com.auggie.student_server.mapper;

import com.auggie.student_server.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OperationLogMapper {

    // 插入日志记录，SQL 语句将放在 XML 文件中
    void insertLog(OperationLog operationLog);

    // 查询所有日志记录
    List<OperationLog> findAll();

    // 根据条件查询日志记录
    List<OperationLog> findByCriteria(@Param("params") Map<String, Object> params);
}