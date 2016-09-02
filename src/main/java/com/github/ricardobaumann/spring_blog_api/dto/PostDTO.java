/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO to handle post IO rest calls
 * @author ricardobaumann 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

	private String category;

	private String title;

	private String content;
	
	@JsonInclude(JsonInclude.Include.NON_NULL) 
	private Long id;

	public PostDTO(String category, String title, String content) {
		super();
		this.category = category;
		this.title = title;
		this.content = content;
	}
	
	
	
}
