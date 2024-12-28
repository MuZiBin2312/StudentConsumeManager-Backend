package com.auggie.student_server.aspect;

import com.auggie.student_server.service.OperationLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Aspect
@Component
public class OperationLogAspect {

    @Autowired
    private OperationLogService logService;

    // 定义切点，拦截所有 Controller 包中的公共方法
    @Pointcut("execution(* com.auggie.student_server.controller..*(..))")
    public void controllerMethods() {
    }

    // 在方法成功执行后记录日志
    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfterSuccess(JoinPoint joinPoint, Object result) {
        // 获取类名和方法名
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        // 获取操作人信息（可以从上下文或 token 中获取，以下为示例值）
        String operator = getOperator(); // 可替换为实际的用户获取逻辑
        String requestId = generateRequestId(); // 替换为实际请求 ID 获取逻辑

        // 调用日志记录服务
        logService.addLog(
                className,
                methodName,
                operator,
                requestId,
                "SUCCESS" // 记录成功状态
        );
    }

    // 在方法抛出异常时记录日志
    @AfterThrowing(pointcut = "controllerMethods()", throwing = "exception")
    public void logAfterFailure(JoinPoint joinPoint, Throwable exception) {
        // 获取类名和方法名
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        // 获取操作人信息（可以从上下文或 token 中获取，以下为示例值）
        String operator = getOperator(); // 可替换为实际的用户获取逻辑
        String requestId = generateRequestId(); // 替换为实际请求 ID 获取逻辑

        // 调用日志记录服务
        logService.addLog(
                className,
                methodName,
                operator,
                requestId,
                "FAILURE: " + exception.getMessage() // 记录失败原因
        );
    }

    // 示例：获取操作人信息
    private String getOperator() {
        // TODO: 替换为实际获取当前操作用户的逻辑，比如从 SecurityContext 或 Token 中获取用户信息
        return "DefaultUser";
    }

    // 示例：生成请求 ID

    private String generateRequestId() {
        // 时间戳部分
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        // 随机数部分
        int randomNum = ThreadLocalRandom.current().nextInt(1000, 9999);
        return timestamp + "-" + randomNum;
    }
}