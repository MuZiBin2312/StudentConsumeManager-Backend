package com.auggie.student_server.mapper;

import com.auggie.student_server.entity.Student;
import com.auggie.student_server.entity.ConsumeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface RecordMapper {

    // 查询所有记录
    List<ConsumeRecord> selectAllRecords();

    int deleteById(int recordId);

    List<ConsumeRecord> findByCriteria(Map<String, Object> criteria);

    // 查询单条记录
    public ConsumeRecord findById(@Param("sid") Integer sid);

    // 查询学生
    public List<Student> findBySearch(@Param("student") Student student, @Param("fuzzy") Integer fuzzy);

    // 更新学生信息
    public boolean updateById(@Param("student") Student student);

    // 插入学生信息
    public boolean save(@Param("student") Student student);

    // 删除学生信息
    public boolean deleteById(@Param("sid") Integer sid);

    // 插入消费记录
    public boolean addRecord(@Param("addRecord") ConsumeRecord consumeRecord);

    // 更新消费记录
    int updateRecordById(ConsumeRecord record);

    // 批量删除消费记录
    int batchDelete(@Param("ids") List<Integer> recordIds);

    // 批量插入消费记录
        int insertBatch(@Param("list") List<ConsumeRecord> records);

}
