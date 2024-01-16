package com.threestar.selectstar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();  // URL 기반의 CORS 구성을 제공
        CorsConfiguration config = new CorsConfiguration();  // 자격 증명 허용하도록 설정
        config.setAllowCredentials(true);  // 요청에 인증 정보를 포함시킬 수 있음

        config.addAllowedOrigin("*");
//        config.addAllowedOrigin("http://localhost:5173");  // 특정 Origin만 허용
//        config.addAllowedOrigin("http://localhost:63342");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader("Authorization");  // Authorization 헤더를 노출하도록 설정 -  브라우저에서 클라이언트로부터 받은 응답 헤더 중 "Authorization"를 읽을 수 있게 함
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
