package com.au.sportsbet.ticket.system.api.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Instant;

/**
 * Interceptor class to log the incoming request and response.
 */
@Slf4j
@Component
public class ApiHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        long currentTime = Instant.now().toEpochMilli();

        request.setAttribute("startTime", currentTime);

        log.info("Incoming Request: method = {}, URI= {}, timestamp={}", request.getMethod(),
                request.getRequestURI(), currentTime);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {

        long duration = System.currentTimeMillis() - (long) request.getAttribute("startTime");

        if (ex != null) {
            log.error("Exception in request, method= {}, URI= {}, timeTaken= {}",
                    request.getMethod(), request.getRequestURI(), duration);
        } else {
            log.info("Response status: method = {}, URI= {}, timeTaken={}", request.getMethod(),
                    request.getRequestURI(), duration);
        }
    }

}
