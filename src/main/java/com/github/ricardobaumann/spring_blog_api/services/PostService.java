package com.github.ricardobaumann.spring_blog_api.services;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.github.ricardobaumann.spring_blog_api.exception.UnauthorizedException;
import com.github.ricardobaumann.spring_blog_api.models.Post;
import com.github.ricardobaumann.spring_blog_api.repositories.PostRepository;

/**
 * Service class for posts
 * @author ricardobaumann
 *
 */
@Service
public class PostService {

	@Autowired
	private PostRepository postRepository;
	
	public Post save(Post post) throws UnauthorizedException {
		if (post.getUsername()==null) {
			throw new UnauthorizedException();
		}
		return postRepository.save(post);
	}

	public Page<Post> findPage(PageRequest pageRequest) {
		return postRepository.findAll(pageRequest);
	}
	
	public Page<Post> findByUsername(String username, PageRequest pageRequest) {
		return postRepository.findByUsername(username, pageRequest);
	}

	public Post find(Long id) {
		return postRepository.findOne(id);
	}

	public void delete(Post post, Principal user) throws UnauthorizedException {
		if (user==null || !post.getUsername().equals(user.getName())) {
			throw new UnauthorizedException();
		}
		postRepository.delete(post);
	}
	
}
