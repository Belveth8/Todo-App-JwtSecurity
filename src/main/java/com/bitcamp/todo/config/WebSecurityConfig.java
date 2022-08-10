package com.bitcamp.todo.config;

import com.bitcamp.todo.security.JwtAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

import static org.springframework.security.config.Customizer.withDefaults;
@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // websecurity 설정
    // web.xml로 httpsecurity를 설정했었는데
    // 현재는 HttpSecurity를 이용한 관련 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .anyRequest().authenticated()
                )
                // http 시큐리티 빌더
                .httpBasic().disable() // token을 사용하므로 basic 인증 disable
                .csrf().disable()
                .cors() // WebMvcConfig에서 이미 설정했으므로 기본 cors 설정.
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // session 기반이 아님을 선언


        // Jwt filter 등록
        // 매 요청마다 CorsFilter 실행한 후에
        // jwtAuthenticationFilter 실행
        http.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/", "/auth/**");
                                            // /와 /auth/** 경로는 인증 안해도 됨.
    }


}
