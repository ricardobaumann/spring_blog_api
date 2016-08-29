/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author ricardobaumann
 *
 */
public class CommentDTO {

	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long id;
	
	private String username;
	
	private String content;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public CommentDTO(Long id, String username, String content) {
		super();
		this.id = id;
		this.username = username;
		this.content = content;
	}

	public CommentDTO() {
		super();
	}

	public CommentDTO(String username, String content) {
		super();
		this.username = username;
		this.content = content;
	}

	
	
}
