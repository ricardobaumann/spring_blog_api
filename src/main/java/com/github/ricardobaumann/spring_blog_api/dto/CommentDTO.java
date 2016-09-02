/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO to map comment endpoints IO calls 
 * @author ricardobaumann
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long id;
	
	private String username;
	
	private String content;

	public CommentDTO(String username, String content) {
		super();
		this.username = username;
		this.content = content;
	}

	
	
}
