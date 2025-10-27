package com.lgcns.studify.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .cors(Customizer.withDefaults())
            // ★ 기본 인증/폼 로그인 비활성화 (WWW-Authenticate: Basic 제거)
            .httpBasic().disable()
            .formLogin().disable()
            .authorizeExchange(ex -> ex
                // ★ 프리플라이트 전체 허용
                .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // ★ 공개 엔드포인트를 실제 컨트롤러 prefix에 맞춤
                .pathMatchers("/api/v1/users/signup").permitAll()
                .pathMatchers("/api/v1/auth/**").permitAll()

                // 기존 post 서비스 오픈 유지
                .pathMatchers("/studify/**").permitAll()
                .pathMatchers("/api/v1/posts/**").permitAll()
                .pathMatchers("/api/v1/post/**").permitAll()

                .pathMatchers("/actuator/**").permitAll()

                // 나머지는 필요한 정책에 맞게 (당장은 전부 오픈해도 됨)
                .anyExchange().permitAll()
            )
            .build();
    }
}