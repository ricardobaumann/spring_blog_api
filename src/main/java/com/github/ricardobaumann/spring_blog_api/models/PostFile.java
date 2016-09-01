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
 * Entity class for files associated to posts
 * @author ricardobaumann
 *
 */
@Entity
public class PostFile {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonInclude(JsonInclude.Include.NON_NULL) 
	private Long id;
	
	@NotNull
	@Size(max=200)
	private String fileName;
	
	@NotNull
	@ManyToOne
	private Post post;

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public PostFile(String fileName, Post post) {
		super();
		this.fileName = fileName;
		this.post = post;
	}

	public PostFile() {
		super();
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
		PostFile other = (PostFile) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
