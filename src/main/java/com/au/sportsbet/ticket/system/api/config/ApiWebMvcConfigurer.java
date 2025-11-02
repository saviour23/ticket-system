package com.au.sportsbet.ticket.system.api.config;

import com.au.sportsbet.ticket.system.api.interceptors.ApiHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Web MVC configure,to register interceptor.
 */
@Configuration
public class ApiWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    ApiHandlerInterceptor apiHandlerInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiHandlerInterceptor);

    }
}
