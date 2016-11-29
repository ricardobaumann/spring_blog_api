/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Comment entity database model
 * @author ricardobaumann
 *
 */
@Entity
public class Comment {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long id;
	
	private String username;
	
	@NotNull
	@Size(max=500)
	private String content;
	
	@NotNull
	@ManyToOne
	private Post post;
	
	public Comment(String username, String content) {
		super();
		this.username = username;
		this.content = content;
	}



	public Comment() {
	}



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



	public Post getPost() {
		return post;
	}



	public void setPost(Post post) {
		this.post = post;
	}
	
	
	
}
