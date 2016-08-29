package com.github.ricardobaumann.spring_blog_api.services;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.ricardobaumann.spring_blog_api.controllers.UnauthorizedException;
import com.github.ricardobaumann.spring_blog_api.models.Comment;
import com.github.ricardobaumann.spring_blog_api.models.Post;
import com.github.ricardobaumann.spring_blog_api.repositories.CommentRepository;

@Service
public class CommentService {
	
	@Autowired
	private CommentRepository commentRepository;

	public Comment save(Comment comment) {
		return commentRepository.save(comment);
	}
	
	public void delete(Comment comment, Principal user) throws UnauthorizedException {
		
		Post post = comment.getPost();
		if (user==null || !user.getName().equals(post.getUsername())) {
			throw new UnauthorizedException();
		}
		
		commentRepository.delete(comment);
	}

	public Comment find(Post post, Long commentId) {
		return commentRepository.findOne(commentId);
	}

	

}
