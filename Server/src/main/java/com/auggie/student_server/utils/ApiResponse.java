package com.auggie.student_server.utils;
import java.io.Serializable;

public class ApiResponse<T> {
    private int code;        // HTTP 状态码
    private boolean success; // 业务成功标志
    private String message;  // 响应消息
    private T data;          // 业务数据

    // 构造方法
    private ApiResponse(int code, String message, boolean success, T data) {
        this.code = code;
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // 静态方法构建成功响应
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "success", true, data);
    }

    // 静态方法构建业务失败响应
    public static <T> ApiResponse<T> businessError(String message) {
        return new ApiResponse<>(200, message, false, null);
    }

    // 静态方法构建系统失败响应
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, false, null);
    }

    // Getter & Setter 省略


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}