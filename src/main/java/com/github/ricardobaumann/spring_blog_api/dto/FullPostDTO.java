/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.dto;

import java.util.List;

/**
 * @author ricardobaumann
 *
 */
public class FullPostDTO extends PostDTO {
	
	public FullPostDTO() {
		super();
	}

	public FullPostDTO(Long id, String category, String title, String content) {
		super(id, category, title, content);
	}

	private List<CommentDTO> comments;

	public List<CommentDTO> getComments() {
		return comments;
	}

	public void setComments(List<CommentDTO> comments) {
		this.comments = comments;
	}
	
}
