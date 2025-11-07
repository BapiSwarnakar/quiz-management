package com.stech.quiz.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    
    @Override
    public void handle(HttpServletRequest request, 
                      HttpServletResponse response, 
                      AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // Set the error status and message in request scope
        request.setAttribute("jakarta.servlet.error.status_code", HttpServletResponse.SC_FORBIDDEN);
        request.setAttribute("jakarta.servlet.error.request_uri", request.getRequestURI());
        request.setAttribute("jakarta.servlet.error.message", "Access is denied");
        
        // Forward to the error controller
        request.getRequestDispatcher("/error").forward(request, response);
    }
}
