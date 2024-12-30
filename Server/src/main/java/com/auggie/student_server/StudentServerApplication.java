package com.auggie.student_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.auggie.student_server.utils.LoadingAnimationUtils;

@SpringBootApplication
public class StudentServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentServerApplication.class, args);
        startLoadingAnimation();  // 调用启动动画方法

    }

    // 启动动画的方法
    private static void startLoadingAnimation() {
        new Thread(() -> {
            try {
                LoadingAnimationUtils.showLoadingAnimation(true); // 启动动画
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}