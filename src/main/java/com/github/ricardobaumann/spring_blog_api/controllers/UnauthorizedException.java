package com.github.ricardobaumann.spring_blog_api.controllers;

public class UnauthorizedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnauthorizedException() {
		
	}

	public UnauthorizedException(String message) {
		super(message);
		
	}

	public UnauthorizedException(Throwable cause) {
		super(cause);
		
	}

	public UnauthorizedException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public UnauthorizedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

}
