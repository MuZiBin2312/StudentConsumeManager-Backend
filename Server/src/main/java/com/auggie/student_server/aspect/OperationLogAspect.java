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

    private static final int STATUS_MAX_LENGTH = 255; // 最大长度

    // 定义切点，拦截所有 Controller 包中的公共方法
    @Pointcut("execution(* com.auggie.student_server.controller..*(..))")
    public void controllerMethods() {
    }

    // 在方法成功执行后记录日志
    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfterSuccess(JoinPoint joinPoint, Object result) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String operator = getOperator();
        String requestId = generateRequestId();
        String host = getClientHost();
        String status = truncate("SUCCESS", STATUS_MAX_LENGTH); // 截取

        log.info("\n<<<----------------------------操作日志---------------------------->>>" +
                        "\n类名: {}\n方法名: {}\n操作人: {}\n请求ID: {}\n主机信息: {}\n状态: {}",
                className, methodName, operator, requestId, host, status);

        logClientInfo();

        logService.addLog(className, methodName, operator, requestId, status, host);
    }

    // 在方法抛出异常时记录日志
    @AfterThrowing(pointcut = "controllerMethods()", throwing = "exception")
    public void logAfterFailure(JoinPoint joinPoint, Throwable exception) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String operator = getOperator();
        String requestId = generateRequestId();
        String host = getClientHost();
        String status = truncate("FAILURE: " + exception.getMessage(), STATUS_MAX_LENGTH); // 截取

        log.error("操作日志（失败）：\n类名: {}\n方法名: {}\n操作人: {}\n请求ID: {}\n主机信息: {}\n状态: {}\n失败原因: {}",
                className, methodName, operator, requestId, host, status, exception.getMessage());

        logClientInfo();

        logService.addLog(className, methodName, operator, requestId, status, host);
    }

    // 截取字符串长度
    private String truncate(String text, int maxLength) {
        if (text == null) {
            return null;
        }
        return text.length() <= maxLength ? text : text.substring(0, maxLength);
    }

    private void logClientInfo() {
        StringBuilder clientInfo = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        clientInfo.append("\n<<<---------------------------客户端信息--------------------------->>>\n");
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            clientInfo.append(headerName).append(": ").append(headerValue).append("\n");
        }
        clientInfo.append("客户端 IP: ").append(getClientIp()).append("\n");
        log.info(clientInfo.toString());
    }

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

    private String getClientHost() {
        String ip = getClientIp();
        int port = request.getRemotePort();
        return ip + ":" + port;
    }

    private String getOperator() {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            if ("userid".equalsIgnoreCase(headerName) && !Objects.equals(headerValue, "")) {
                return request.getHeader(headerName);
            }
        }
        return "UnknownUser";
    }

    private String generateRequestId() {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        int randomNum = ThreadLocalRandom.current().nextInt(1000, 9999);
        return timestamp + "-" + randomNum;
    }
}
