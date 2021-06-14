package com.tengshan.blog.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //允许放行
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/admin/**","/toIndex")
                .excludePathPatterns("/admin")
                .excludePathPatterns("/admin/login")
                .excludePathPatterns("/admin/register")
                .excludePathPatterns("/admin/manageLogin");
    }
}
