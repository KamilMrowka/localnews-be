package com.kamil.dev.local.news.demo.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MigrationAuthFilter implements Filter {
    private static final String MIGRATION_KEY_HEADER = "X-Migration-Key";

    private final String migrationSecretKey;

    public MigrationAuthFilter(@Value("${MIGRATION_SECRET_KEY}") String migrationSecretKey) {
        this.migrationSecretKey = migrationSecretKey;
    }

    @Override
    public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (httpRequest.getDispatcherType().name().equals("REQUEST")) {
            if (httpRequest.getRequestURI().startsWith("/api/v1/migrate")) {
                String migrationKey = httpRequest.getHeader(MIGRATION_KEY_HEADER);

                if (!migrationSecretKey.equals(migrationKey)) {
                    httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    httpResponse.getWriter().write("Forbidden: Invalid Migration Key");
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
