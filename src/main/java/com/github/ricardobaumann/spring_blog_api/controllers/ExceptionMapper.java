/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.controllers;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.ricardobaumann.spring_blog_api.dto.Error;
import com.github.ricardobaumann.spring_blog_api.exception.NotFoundException;
import com.github.ricardobaumann.spring_blog_api.exception.UnauthorizedException;

/**
 * @author ricardobaumann
 *
 */
@RestController
@ControllerAdvice
public class ExceptionMapper {

	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)  
    @ExceptionHandler(value = {ValidationException.class, 
    		ConstraintViolationException.class, 
    		DataIntegrityViolationException.class,
    		TransactionSystemException.class}) 
    public @ResponseBody Error handleUnprocessableEntity(Throwable ex) {
       return new Error(ex.getCause()!=null ? ex.getCause().getMessage() : ex.getMessage());//TODO handle a better and understable message
    }
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public @ResponseBody Error handleException(Throwable t) {
		return new Error(t.getCause().getMessage());
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NotFoundException.class)
	public @ResponseBody Error handleNotFound(Throwable t) {
		return null;
	}
	
	@ResponseStatus(code=HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(UnauthorizedException.class)
	public @ResponseBody Error handleUnauthorized(Throwable t) {
		return null;
	}

	
}
