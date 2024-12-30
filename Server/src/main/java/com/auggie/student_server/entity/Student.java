package com.auggie.student_server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

/**
 * @Auther: auggie
 * @Date: 2022/2/8 16:11
 * @Description: Student
 * @Version 1.0.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Alias("Student")
public class Student {
    private Integer sid;             // 学生ID
    private String sname;            // 学生姓名
    private String password;         // 密码
    private String idNumber;         // 身份证号
    private String ethnicity;        // 民族
    private String college;          // 学院
    private String politicalStatus;  // 政治面貌
    private String grade;            // 年级
    private String subject;          // 学科
    private String campus;           // 校区
}
