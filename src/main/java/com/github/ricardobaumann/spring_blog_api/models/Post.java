/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.models;

import java.util.List;

import javax.persistence.*;
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
	@Size(max=50)
	private String username;
	
	@OneToMany(cascade=CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JsonInclude(JsonInclude.Include.NON_NULL) 
	private List<Comment> comments;

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Post(String category, String title, String content) {
		super();
		this.category = category;
		this.title = title;
		this.content = content;
	}
	
	public Post(String category, String title, String content, String username) {
		super();
		this.category = category;
		this.title = title;
		this.content = content;
		this.username = username;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Post other = (Post) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
