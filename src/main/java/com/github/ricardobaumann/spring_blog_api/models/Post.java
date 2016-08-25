/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Model entity for post data
 * @author ricardobaumann
 *
 */
@Entity
public class Post {

	@Id
	private Long id;
	
	@Column(length=50,nullable=false)
	private String category;
	
	@Column(length=100, nullable=false)
	private String title;
	
	@Column(length=1000, nullable=false)
	private String content;

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
	
}
