package com.auggie.student_server.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {

    public static void printAuthenticationInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            System.out.println("当前没有认证信息。");
            return;
        }

        System.out.println("认证类型: " + authentication.getClass().getSimpleName());
        System.out.println("是否已认证: " + authentication.isAuthenticated());
        System.out.println("认证主体 (Principal): " + authentication.getPrincipal());
        System.out.println("权限 (Authorities): " + authentication.getAuthorities());
        System.out.println("认证详情 (Details): " + authentication.getDetails());

        // 如果主体是 UserDetails 类型，可以打印更多信息
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            System.out.println("用户名: " + userDetails.getUsername());
            System.out.println("密码: " + userDetails.getPassword());  // 通常为加密后的密码
            System.out.println("账户是否未过期: " + userDetails.isAccountNonExpired());
            System.out.println("账户是否未锁定: " + userDetails.isAccountNonLocked());
            System.out.println("凭证是否未过期: " + userDetails.isCredentialsNonExpired());
            System.out.println("账户是否启用: " + userDetails.isEnabled());
        }
    }
}
