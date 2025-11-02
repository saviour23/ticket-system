package com.au.sportsbet.ticket.system.api.filter;

import com.au.sportsbet.ticket.system.api.constants.ApiConstant;
import com.au.sportsbet.ticket.system.api.util.ApiUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class ApiHeaderFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        final HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String correlationId = Optional.ofNullable(httpServletRequest.getHeader(ApiConstant.CORRELATION_ID))
                .orElseGet(ApiUtils::getCorrelationId);
        MDC.put(ApiConstant.CORRELATION_ID, correlationId);

        //setting response HEADERS
        httpServletResponse.addHeader(ApiConstant.HEADER_X_CORRELATION_ID, correlationId);
        filterChain.doFilter(servletRequest, servletResponse);

    }
}
