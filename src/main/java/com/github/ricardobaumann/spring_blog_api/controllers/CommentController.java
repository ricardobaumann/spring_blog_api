/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.controllers;

import java.security.Principal;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.ricardobaumann.spring_blog_api.dto.CommentDTO;
import com.github.ricardobaumann.spring_blog_api.helpers.PostHelper;
import com.github.ricardobaumann.spring_blog_api.models.Comment;
import com.github.ricardobaumann.spring_blog_api.repositories.CommentRepository;
import com.github.ricardobaumann.spring_blog_api.repositories.PostRepository;

/**
 * @author ricardobaumann
 *
 */
@RestController
@RequestMapping(path="/posts/{post_id}/comments")
public class CommentController extends BaseController {
	
	@Autowired
	private PostHelper postHelper;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseStatus(code=HttpStatus.CREATED)
	public @ResponseBody CommentDTO create(@RequestBody CommentDTO commentDTO, 
			@PathVariable("post_id") Long postId, Principal user) {
		Comment comment = postHelper.fromCommentDto(commentDTO);
		comment.setPost(postRepository.findOne(postId));
		if (user!=null) {
			comment.setUsername(user.getName());
		}
		comment = commentRepository.save(comment);
		
		return postHelper.toCommentDTO(comment);
	}
	
}
