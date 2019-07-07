package com.arrow.kronos.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.CsrfException;

public class KronosAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if (!response.isCommitted()) {
            if (accessDeniedException instanceof CsrfException) {
                // Send HTTP 401 to indicate the web app should be reloaded to refresh CSRF token
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, accessDeniedException.getMessage());
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
            }
        }
    }

}
