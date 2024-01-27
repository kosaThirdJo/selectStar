package com.threestar.selectstar.config;

import com.threestar.selectstar.config.jwt.JwtAuthenticationFilter;
import com.threestar.selectstar.config.jwt.JwtAuthorizationFilter;
import com.threestar.selectstar.config.jwt.JwtProvider;
import com.threestar.selectstar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
//@EnableMethodSecurity(prePostEnabled = true)  //  메소드 기반의 보안을 활성화
public class SpringSecurityConfig {

    private final UserRepository userRepository;
    private CorsConfig corsConfig;
    private AuthenticationConfiguration authenticationConfiguration;
    private final JwtProvider jwtProvider;

    @Autowired
    public SpringSecurityConfig(UserRepository userRepository, CorsConfig corsConfig, AuthenticationConfiguration authenticationConfiguration, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.corsConfig = corsConfig;
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtProvider = jwtProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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
                .addFilter(corsConfig.corsFilter())
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .formLogin().disable()
                    .httpBasic().disable()

                .logout(logoutConfigurer -> logoutConfigurer // 로그아웃 설정 변경
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // 로그아웃 요청 URL 변경
                )

                // JWT 인증 및 인가 필터를 추가
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilter(jwtAuthorizationFilter())

                // 요청에 대한 권한 설정
                .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/**","/assets/**","/","/index.html","/meeting","/apply/**","/meeting/**","/comment/meeting/**","/users/**" ,"/login","/logout","/rankMeeting", "/checkDuplicate", "/profiles/**", "/profiles/info/**","/users/validate-token").permitAll()  // 인증 필요 없음
//                 .requestMatchers("/admin/**").hasRole("ADMIN")  // 관리자 구현 예정
                .anyRequest().authenticated()  // 나머지는 인증 필요
                );
        return httpsecurity.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(jwtProvider, authenticationManagerBean());
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
        return new JwtAuthorizationFilter(authenticationManagerBean(), userRepository, jwtProvider);
    }
}