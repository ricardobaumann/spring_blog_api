/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * DTO to handle post IO rest calls
 * @author ricardobaumann 
 */
public class PostDTO {

	private String category;

	private String title;

	private String content;
	
	@JsonInclude(JsonInclude.Include.NON_NULL) 
	private Long id;
	
	public PostDTO() {
	}

	public PostDTO(Long id, String category, String title, String content) {
		super();
		this.category = category;
		this.title = title;
		this.content = content;
		this.id = id;
	}

	public PostDTO(String category, String title, String content) {
		this(null,category,title,content);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
