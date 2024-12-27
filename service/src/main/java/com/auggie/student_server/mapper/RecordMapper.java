package com.auggie.student_server.mapper;

import com.auggie.student_server.entity.Student;
import com.auggie.student_server.entity.ConsumeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface RecordMapper {

    /**
     * @Auther: auggie
     * @Date: 2022/2/8 16:12
     * @Description: StudentMapper
     * @Version 1.0.0
     */

    //    select
    List<ConsumeRecord> selectAllRecords();

    int deleteById(int recordId);

    List<ConsumeRecord> findByCriteria(Map<String, Object> criteria);

    public ConsumeRecord findById(@Param("sid") Integer sid);

    public List<Student> findBySearch(@Param("student") Student student, @Param("fuzzy") Integer fuzzy);

    //    update
    public boolean updateById(@Param("student") Student student);

    //    insert
    public boolean save(@Param("student") Student student);

    //    delete
    public boolean deleteById(@Param("sid") Integer sid);

    //  add record
    public boolean addRecord(@Param("addRecord") ConsumeRecord consumeRecord);


    int updateRecordById(ConsumeRecord record);
}
