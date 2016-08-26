/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.models;

import org.springframework.stereotype.Component;

import com.github.ricardobaumann.spring_blog_api.dto.CommentDTO;

/**
 * @author ricardobaumann
 *
 */
@Component
public class CommentHelper {

	public Comment fromDto(CommentDTO commentDTO) {
		return new Comment(commentDTO.getUsername(), commentDTO.getContent());
	}

	public CommentDTO toDTO(Comment comment) {
		return new CommentDTO(comment.getContent(), comment.getId());
	}

}
