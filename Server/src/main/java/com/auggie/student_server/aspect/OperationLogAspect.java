package com.auggie.student_server.aspect;

import com.auggie.student_server.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Aspect
@Component
public class OperationLogAspect {

    @Autowired
    private OperationLogService logService;

    @Autowired
    private HttpServletRequest request;

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
        String operator = getOperator();
        String requestId = generateRequestId();
        String host = getClientHost();

        // 打印操作日志
        log.info("\n<<<----------------------------操作日志---------------------------->>>" +
                        "\n类名: {}\n方法名: {}\n操作人: {}\n请求ID: {}\n主机信息: {}\n状态: SUCCESS",
                className, methodName, operator, requestId, host);

        // 打印客户端信息
        logClientInfo();

        // 调用日志记录服务
        logService.addLog(className, methodName, operator, requestId, "SUCCESS", host);
    }

    // 在方法抛出异常时记录日志
    @AfterThrowing(pointcut = "controllerMethods()", throwing = "exception")
    public void logAfterFailure(JoinPoint joinPoint, Throwable exception) {
        // 获取类名和方法名
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String operator = getOperator();
        String requestId = generateRequestId();
        String host = getClientHost();

        // 打印操作日志
        log.error("操作日志（失败）：\n类名: {}\n方法名: {}\n操作人: {}\n请求ID: {}\n主机信息: {}\n状态: FAILURE\n失败原因: {}",
                className, methodName, operator, requestId, host, exception.getMessage());

        // 打印客户端信息
        logClientInfo();

        // 调用日志记录服务
        logService.addLog(className, methodName, operator, requestId, "FAILURE: " + exception.getMessage(), host);
    }

    // 打印客户端信息
    private void logClientInfo() {
        StringBuilder clientInfo = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        clientInfo.append("\n<<<---------------------------客户端信息--------------------------->>>\n");
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            clientInfo.append(headerName).append(": ").append(headerValue).append("\n");
        }

        // 打印 IP 地址
        String clientIp = getClientIp();
        clientInfo.append("客户端 IP: ").append(clientIp).append("\n");

        log.info(clientInfo.toString());
    }

    // 获取客户端 IP 地址
    private String getClientIp() {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    // 获取客户端主机信息（IP:端口）
    private String getClientHost() {
        String ip = getClientIp();
        int port = request.getRemotePort();
        return ip + ":" + port;
    }

    // 获取操作人信息
    private String getOperator() {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            if ("userid".equalsIgnoreCase(headerName)&& !Objects.equals(headerValue, "")) {
                return request.getHeader(headerName);
            }
        }
        return "UnknownUser";
    }

    // 生成请求 ID
    private String generateRequestId() {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        int randomNum = ThreadLocalRandom.current().nextInt(1000, 9999);
        return timestamp + "-" + randomNum;
    }
}