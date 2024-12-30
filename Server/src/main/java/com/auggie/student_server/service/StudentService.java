package com.auggie.student_server.service;

import com.auggie.student_server.entity.Student;
import com.auggie.student_server.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: auggie
 * @Date: 2022/2/9 08:27
 * @Description: StudentService
 * @Version 1.0.0
 */

@Service
public class StudentService {
    @Autowired
    private StudentMapper studentMapper;

    /**
     * 分页查询学生列表
     *
     * @param num  第几页，从零开始
     * @param size 每页大小
     * @return 分页后的学生列表
     */
    public List<Student> findByPage(Integer num, Integer size) {
        List<Student> studentList = studentMapper.findAll();
        ArrayList<Student> list = new ArrayList<>();

        int start = size * num;
        int end = size * (num + 1);
        int sz = studentList.size();

        for (int i = start; i < end && i < sz; i++) {
            list.add(studentList.get(i));
        }

        return list;
    }

    /**
     * 根据条件搜索学生
     *
     * @param sid   学生ID
     * @param sname 学生姓名
     * @param fuzzy 是否模糊查询（0: 否, 1: 是）
     * @return 查询结果列表
     */
    public List<Student> findBySearch(Integer sid, String sname, Integer fuzzy) {
        Student student = new Student();
        student.setSid(sid);
        student.setSname(sname);
        fuzzy = (fuzzy == null) ? 0 : fuzzy;

        return studentMapper.findBySearch(student, fuzzy);
    }

    /**
     * 获取学生总数
     *
     * @return 学生总数
     */
    public Integer getLength() {
        return studentMapper.findAll().size();
    }

    /**
     * 根据ID查询学生
     *
     * @param sid 学生ID
     * @return 学生对象
     */
    public Student findById(Integer sid) {
        return studentMapper.findById(sid);
    }

    /**
     * 根据ID更新学生信息
     *
     * @param student 学生对象
     * @return 是否成功
     */
    public boolean updateById(Student student) {
        return studentMapper.updateById(student);
    }

    /**
     * 新增学生
     *
     * @param student 学生对象（需要提供新增字段的值）
     * @return 是否成功
     */
    public boolean save(Student student) {
        // 确保传入的 student 对象包含所有必填字段值
        return studentMapper.save(student);
    }

    /**
     * 根据ID删除学生
     *
     * @param sid 学生ID
     * @return 是否成功
     */
    public boolean deleteById(Integer sid) {
        return studentMapper.deleteById(sid);
    }
}
