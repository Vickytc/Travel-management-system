package com.travel.admin.exception;

import com.travel.admin.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 * Global Exception Handler
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionhandler {
    /**
     * Custom business exception handling
     */
    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException ex) {
        return new Result(ex.getErrorCode() == null ? 500 : ex.getErrorCode(), ex.getMessage(), null);
    }

    /**
     * Handle MethodArgumentNotValidException
     *
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Incorrect method argument", e);
        return Result.error(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * Handle ConstraintViolationException
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result handleConstraintViolationException(ConstraintViolationException e) {
        log.error("Parameter error", e);
        return Result.error(e.getConstraintViolations().iterator().next().getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception exception){
        exception.printStackTrace();
        return Result.error("System error, please try again later!");
    }
}
