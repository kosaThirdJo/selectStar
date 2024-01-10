package com.threestar.selectstar.config;

import com.threestar.selectstar.config.jwt.JwtAuthenticationFilter;
import com.threestar.selectstar.config.jwt.JwtAuthorizationFilter;
import com.threestar.selectstar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
//@EnableMethodSecurity(prePostEnabled = true)  //  메소드 기반의 보안을 활성화
public class SpringSecurityConfig {

    private final UserRepository userRepository;
    private CorsConfig corsConfig;
    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    public SpringSecurityConfig(UserRepository userRepository, CorsConfig corsConfig, AuthenticationConfiguration authenticationConfiguration) {
        this.userRepository = userRepository;
        this.corsConfig = corsConfig;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    // 비밀번호 암호화
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager : 인증을 담당하는 매니저
    // 인증에 대한 부분은 Spring Security의 AuthenticationManager를 통해서 처리하게 되는데,
    // 실질적으로는 AuthenticationManager에 등록된 AuthenticationProvider에 의해서 처리됨
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public WebSecurityCustomizer configure() {
        return (web -> web.ignoring().requestMatchers("/search/**", "/users/checkDuplicate"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpsecurity) throws Exception {
        httpsecurity
                .addFilter(corsConfig.corsFilter()) // Spring Security 사용시 CORS 설정을 하기 위해서 Authentication Filter 인증보다 앞에 필터를 추가
                    .csrf().disable()  // CSRF 보호 비활성화
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않도록 설정
                .and()
                    // 로그인 폼과 HTTP 기본 인증 비활성화
                    .formLogin().disable()  // Security가 자동으로 지원하는 로그인폼을 사용하지 않겠다는 의미
                    .httpBasic().disable()  // JWT 방식을 사용할 예정이니 비활성화 - Bearer

                    // JWT 인증 및 인가 필터를 추가
                    .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)  // JWT 인증 필터
                    .addFilter(jwtAuthorizationFilter())  // JWT 인가 필터

                // 요청에 대한 권한 설정
                .authorizeHttpRequests(authorize -> authorize  // authorizeRequests() : deprecated로 authorizeHttpRequest() 사용
                .requestMatchers("/**","/assets/**","/","/index.html","/meeting","/apply/**","/meeting/**","/comment/meeting/**","/users/**" ,"/login","/rankMeeting", "/checkDuplicate", "/profiles/**", "/profiles/info/**").permitAll()  // 인증 필요 없음
//                 .requestMatchers("/admin/**").hasRole("ADMIN")  // 관리자 구현 예정
                .anyRequest().authenticated()  // 나머지는 인증 필요
                );
        return httpsecurity.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        System.out.println("jwtAuthenticationFilter 등록");
        return new JwtAuthenticationFilter(authenticationManagerBean());
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
        return new JwtAuthorizationFilter(authenticationManagerBean(), userRepository);
    }
}