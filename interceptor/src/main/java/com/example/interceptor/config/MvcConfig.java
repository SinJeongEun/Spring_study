package com.example.interceptor.config;

import com.example.interceptor.interceptor.AuthInterceptor;
import jdk.jfr.Frequency;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor);
//        registry.addInterceptor(authInterceptor).addPathPatterns("api/private/*"); : 검사하고싶은 url만 적용하기
//        registry.addInterceptor(authInterceptor).excludePathPatterns();  배제할 url 설정하기
    }
}
