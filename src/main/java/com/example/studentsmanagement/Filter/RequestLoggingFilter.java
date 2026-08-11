package com.example.studentsmanagement.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(1)
public class RequestLoggingFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String endpoint = request.getMethod() + " " + request.getRequestURI();
            String requestId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            MDC.put("requestId", requestId);
            MDC.put("endpoint", endpoint);
            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            logger.error("Request failed", exception);
            throw exception;
        } finally {
            MDC.remove("requestId");
        }
    }
}
