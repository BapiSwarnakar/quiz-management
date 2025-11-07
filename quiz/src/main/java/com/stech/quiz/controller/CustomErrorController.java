package com.stech.quiz.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/error")
public class CustomErrorController implements ErrorController {

    
    @GetMapping
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            try {
                int statusCode = Integer.parseInt(status.toString());
                
                if (statusCode == HttpStatus.FORBIDDEN.value()) {
                    return "error/403";
                } else if (statusCode == HttpStatus.NOT_FOUND.value()) {
                    return "error/404";
                } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                    return "error/500";
                }
            } catch (NumberFormatException e) {
                // If status code is not a valid number, fall through to default error page
            }
        }
        // Default error page for any other error
        return "error/error";
    }
}
