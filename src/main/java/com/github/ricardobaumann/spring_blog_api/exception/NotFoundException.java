/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.exception;

/**
 * Exception class to handle NOT_FOUND status codes on endpoints
 * @author ricardobaumann
 *
 */
public class NotFoundException extends Exception {

	
	private static final long serialVersionUID = 1L;

	
	public NotFoundException() {
	}

	
	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(Throwable cause) {
		super(cause);
	}

	public NotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
