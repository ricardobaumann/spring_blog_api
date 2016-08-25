package com.github.ricardobaumann.spring_blog_api.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestController
public class BaseController {
	
	
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)  
    @ExceptionHandler(value = {ValidationException.class, 
    		ConstraintViolationException.class, 
    		DataIntegrityViolationException.class,
    		TransactionSystemException.class}) 
    Error handleUnprocessableEntity(Throwable ex) {
       return new Error(ex.getCause()!=null ? ex.getCause().getMessage() : ex.getMessage());
    }
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public @ResponseBody Error handleException(Throwable t) {
		return new Error(t.getCause().getMessage());
	}


}
