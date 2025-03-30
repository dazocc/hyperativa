package com.dazo.hyperativa.authentication.log;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
@WebFilter("/*")
@Slf4j
public class RequestLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        Instant startTime = Instant.now();

        log.info("Iniciando requisição: Method = {}, URI = {}",
                httpRequest.getMethod(),
                httpRequest.getRequestURI());

        chain.doFilter(request, response);

        Instant endTime = Instant.now();
        long duration = endTime.toEpochMilli() - startTime.toEpochMilli();

        log.info("Requisição finalizada: Method = {}, URI = {}, Status = {}, Duração = {} ms",
                httpRequest.getMethod(),
                httpRequest.getRequestURI(),
                ((HttpServletResponse) response).getStatus(),
                duration);
    }

}