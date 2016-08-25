/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.helpers;

import org.springframework.stereotype.Component;

import com.github.ricardobaumann.spring_blog_api.dto.PostDTO;
import com.github.ricardobaumann.spring_blog_api.models.Post;

/**
 * Helpers for the rest controllers
 * @author ricardobaumann
 *
 */
@Component
public class PostHelper {

	public Post from(PostDTO postDTO) {
		return new Post(postDTO.getCategory(),postDTO.getTitle(),postDTO.getContent());
	}

	public PostDTO toDTO(Post post) {
		return new PostDTO(post.getId(),post.getCategory(), post.getTitle(), post.getContent());
	}
	
}
