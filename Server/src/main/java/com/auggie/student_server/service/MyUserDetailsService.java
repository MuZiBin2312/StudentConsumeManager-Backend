package com.auggie.student_server.service;


import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 这里根据用户名从数据库或其他地方加载用户信息
        // 例如，你可以使用 UserRepository 查找数据库中的用户
        if ("admin".equals(username)) {
            return User.builder()
                    .username("admin")
                    .password("$2a$10$Cq8pBp27F0rL5yB8Yb5EuIrrUOnXnOX0pHKq/Yq2mL2.Bc4u8N1eS") // BCrypt加密的密码
                    .roles("ADMIN")
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}