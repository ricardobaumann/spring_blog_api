/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.controllers;

import java.security.Principal;

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
import com.github.ricardobaumann.spring_blog_api.exception.NotFoundException;
import com.github.ricardobaumann.spring_blog_api.exception.UnauthorizedException;
import com.github.ricardobaumann.spring_blog_api.helpers.PostHelper;
import com.github.ricardobaumann.spring_blog_api.models.Comment;
import com.github.ricardobaumann.spring_blog_api.models.Post;
import com.github.ricardobaumann.spring_blog_api.services.CommentService;
import com.github.ricardobaumann.spring_blog_api.services.PostService;

/**
 * Controller for comment endpoints
 * @author ricardobaumann
 *
 */
@RestController
@RequestMapping(path="/posts/{post_id}/comments")
public class CommentController extends BaseController {
	
	@Autowired
	private PostHelper postHelper;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private PostService postService;
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseStatus(code=HttpStatus.CREATED)
	public @ResponseBody CommentDTO create(@RequestBody CommentDTO commentDTO, 
			@PathVariable("post_id") Long postId, Principal user) {
		Comment comment = postHelper.fromCommentDto(commentDTO);
		comment.setPost(postService.find(postId));
		if (user!=null) {
			comment.setUsername(user.getName());
		}
		comment = commentService.save(comment);
		
		return postHelper.toCommentDTO(comment);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, path="{comment_id}")
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("post_id") Long postId,
			@PathVariable("comment_id") Long commentId,
			Principal user) throws NotFoundException, UnauthorizedException {
		
		Post post = postService.find(postId);
		if (post==null) {
			throw new NotFoundException();
		}
		Comment comment = commentService.find(post, commentId);
		if (comment==null) {
			throw new NotFoundException();
		}
		commentService.delete(comment, user);
	}
	
}
