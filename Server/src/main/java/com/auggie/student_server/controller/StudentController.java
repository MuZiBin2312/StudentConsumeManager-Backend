package com.auggie.student_server.controller;

import com.auggie.student_server.entity.Student;
import com.auggie.student_server.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: auggie
 * @Date: 2022/2/8 17:37
 * @Description: StudentController
 * @Version 1.0.0
 */

@RestController
@CrossOrigin("*")
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @PostMapping("/addStudent")
    public boolean addStudent(@RequestBody Student student) {
        System.out.println("正在保存学生对象" + student);
        return studentService.save(student);
    }

    @PostMapping("/login")
    public boolean login(@RequestBody Student student, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        System.out.println("正在验证学生登录: " + student);

        Student s = studentService.findById(student.getSid());
        if (s == null || !s.getPassword().equals(student.getPassword())) {
            response.put("message", "登录失败，账号或密码错误");
            response.put("code", 401);
            return true;
        }

        // 构建权限集合，假设学生有一个默认角色 "ROLE_STUDENT"
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_STUDENT"));

        // 认证成功，手动设置认证信息
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(s.getSid(), null, authorities);

        // 将认证信息存入 SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 将认证信息存入 HttpSession
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());

        // 构建返回信息
        response.put("message", "登录成功");
        Map<String, Object> data = new HashMap<>();
        data.put("sid", s.getSid());
        data.put("name", s.getSname());
        data.put("sessionId", request.getSession().getId());
        response.put("data", data);
        response.put("code", 200);

        // 打印日志
        System.out.println("认证成功，返回重要信息：");
        System.out.println("认证主体 (Principal): " + authentication.getPrincipal());
        System.out.println("权限 (Authorities): " + authentication.getAuthorities());
        System.out.println("认证详细信息 (Details): " + authentication.getDetails());
        System.out.println("返回内容: " + response);

        return true;
    }

    @PostMapping("/findBySearch")
    public List<Student> findBySearch(@RequestBody Student student) {
        Integer fuzzy = (student.getPassword() == null) ? 0 : 1;
        return studentService.findBySearch(student.getSid(), student.getSname(), fuzzy);
    }

    @GetMapping("/findById/{sid}")
    public Student findById(@PathVariable("sid") Integer sid) {
        System.out.println("正在查询学生信息 By id " + sid);
        return studentService.findById(sid);
    }

    @GetMapping("/findByPage/{page}/{size}")
    public List<Student> findByPage(@PathVariable("page") int page, @PathVariable("size") int size) {
        System.out.println("查询学生列表分页 " + page + " " + size);
        return studentService.findByPage(page, size);
    }

    @GetMapping("/getLength")
    public Integer getLength() {
        return studentService.getLength();
    }

    @GetMapping("/deleteById/{sid}")
    public boolean deleteById(@PathVariable("sid") int sid) {
        System.out.println("正在删除学生 sid：" + sid);
        return studentService.deleteById(sid);
    }

    @PostMapping("/updateStudent")
    public boolean updateStudent(@RequestBody Student student) {
        System.out.println("更新 " + student);
        return studentService.updateById(student);
    }
}
