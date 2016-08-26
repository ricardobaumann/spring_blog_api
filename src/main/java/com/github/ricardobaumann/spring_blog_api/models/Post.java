/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Model entity for post data
 * @author ricardobaumann
 *
 */
@Entity
public class Post {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonInclude(JsonInclude.Include.NON_NULL) 
	private Long id;
	
	@NotNull
	@Size(max=50)
	private String category;
	
	@NotNull
	@Size(max=100)
	private String title;
	
	@NotNull
	@Size(max=1000)
	private String content;
	
	@NotNull
	@JsonIgnore
	private String username;

	public Post(String category, String title, String content) {
		super();
		this.category = category;
		this.title = title;
		this.content = content;
	}
	
	public Post() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
