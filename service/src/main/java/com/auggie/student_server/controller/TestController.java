package com.auggie.student_server.controller;

import com.auggie.student_server.entity.Student;
import com.auggie.student_server.entity.Teacher;
import com.auggie.student_server.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Auther: auggie
 * @Date: 2022/2/9 11:02
 * @Description: TeacherController
 * @Version 1.0.0
 */

@RestController
@CrossOrigin("*")
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/log")
    public boolean log() {
        System.out.printf("log");
        return true;
    }
}
