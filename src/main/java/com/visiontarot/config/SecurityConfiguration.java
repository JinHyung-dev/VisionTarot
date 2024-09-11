package com.visiontarot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/**").permitAll() // 모든 요청 허용
                                .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (필요에 따라 설정)
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login") // 로그인 페이지 설정
                                .permitAll() // 모든 사용자가 로그인 페이지에 접근할 수 있도록 허용
                );

        return http.build();
    }
}
