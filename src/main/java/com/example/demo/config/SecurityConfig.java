package com.example.demo.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {




    private final AuthenticationFailureHandler LoginFailureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.setMatchingRequestParameterName(null);

        http.csrf().disable()
                .requestCache((cache) -> cache
                        .requestCache(requestCache)
                )
                .authorizeHttpRequests()
                .requestMatchers("/login", "/register","/css/**", "/js/**", "/assets/**", "/upload", "/post/**").permitAll()
                .requestMatchers("/user/*").hasRole("MEMBER")
                .anyRequest().authenticated()
                .and()

                .headers()
                .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                .and()
                .formLogin()
                
                // 로그인 페이지
                .loginPage("/login")
                
                // email 파라메터
                .usernameParameter("email")
                
                // password 파라메터
                .passwordParameter("password")

                // 로그인 액션 URL
                .loginProcessingUrl("/login")

                // 로그인 성공 시 URL
                .defaultSuccessUrl("/user/home")
                // 로그인 실패 시 URL
//                .failureUrl("/login/failed")
                
                // 로그인 실패 시 실행할 커스텀 핸들러
                .failureHandler(LoginFailureHandler)
        		.and()

                // 로그아웃 설정
                .logout()

                // 로그아웃 경로
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))

                // 로그아웃 성공 시 URL
                .logoutSuccessUrl("/login")

                .invalidateHttpSession(true)
                .and()

                .exceptionHandling().accessDeniedPage("/login/failed");

        return http.build();
    }

}
