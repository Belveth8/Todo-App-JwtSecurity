package com.bitcamp.todo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// CrossOrigin 은 모든 포트들을 다 받아들인다는 것
// 그래서 config로 내가 지정할 포트에서만 req res만 하는 걸 지정
// WebMvcConfigurer 상속받기
@Configuration  // 스프링 bean으로 등록 ( bean : 인스턴스화 된 객체 / beans : 그걸 담고 있는 컨테이너 )
public class WebMvcConfig implements WebMvcConfigurer {
    // 상수
    private final long MAX_AGE_SECS = 3600;
    
    // overriding
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4001","http://192.168.219.101:4001","http://192.168.0.185:4001")
                .allowedMethods("GET","POST","PUT","DELETE","PATCH","OPTIONS")
                .allowedHeaders("*")
                .maxAge(MAX_AGE_SECS); //유효시간
    }
}
