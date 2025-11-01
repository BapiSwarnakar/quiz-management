package com.stech.quiz.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException(ResourceNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        model.addAttribute("status", 404);
        return "error/not-found";
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequestException(BadRequestException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        model.addAttribute("status", 400);
        return "error/bad-request";
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleUnauthorizedException(UnauthorizedException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        model.addAttribute("status", 401);
        return "error/unauthorized";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception ex, Model model) {
        model.addAttribute("error", "An unexpected error occurred: " + ex.getMessage());
        model.addAttribute("status", 500);
        return "error/general";
    }
}
