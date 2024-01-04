package com.example.luvinajwt.config;

import com.example.luvinajwt.jwt.JwtFilter;
import com.example.luvinajwt.model.Role;
import com.example.luvinajwt.service.Impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    private final UserServiceImpl userServiceImp;

    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    private final CustomExceptionHandler customExceptionHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Nếu không custom cái này spring cũng sẽ tự tìm đến các bean default để config
    // Nhưng mình muốn đảm bảo nó chạy đúng instance mà mình muốn, đúng kiểm mã hóa pass để tránh conflic giữa khi giải mã và mã hóa.
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userServiceImp);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request -> request.requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/admin").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/user").hasAnyAuthority(Role.USER.name())
                        .anyRequest().authenticated())
                // Làm việc không quan tâm đến session, mọi request truyền lên đều phải có đủ thông tin token để xác thực
                // Thích hợp cho các ứng dụng dùng RESTful API
                // Trước đây khi chưa có jwt việc xác thực sẽ dựa vào id của các phiên làm việc được lưu vào trong db dẫn đến tốn nhiều dung lượng + mất time
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(
                        ex -> ex.accessDeniedHandler(customAccessDeniedHandler)
                                .authenticationEntryPoint(customExceptionHandler));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
