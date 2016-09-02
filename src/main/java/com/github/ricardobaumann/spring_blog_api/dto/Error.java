/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Error dto for rest controllers
 * @author ricardobaumann
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Error {

	private String message;
	
}
