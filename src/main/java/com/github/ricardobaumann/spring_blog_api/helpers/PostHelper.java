/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.helpers;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
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

	public List<PostDTO> toDTOList(Page<Post> page) {
		return page.getContent().stream().map(post -> toDTO(post)).collect(Collectors.toList());
	}
	
}
