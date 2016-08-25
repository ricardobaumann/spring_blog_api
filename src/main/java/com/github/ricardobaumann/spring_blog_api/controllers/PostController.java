/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.ricardobaumann.spring_blog_api.dto.PostDTO;
import com.github.ricardobaumann.spring_blog_api.helpers.PostHelper;
import com.github.ricardobaumann.spring_blog_api.models.Post;
import com.github.ricardobaumann.spring_blog_api.repositories.PostRepository;

/**
 * Rest controller service for posts
 * @author ricardobaumann
 *
 */
@RestController
@RequestMapping(path="/posts")
public class PostController extends BaseController {

	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private PostHelper postHelper;
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseStatus(code=HttpStatus.CREATED)
	@ResponseBody
	public PostDTO create(@RequestBody PostDTO postDTO) {
		Post post = postHelper.from(postDTO);
		post = postRepository.save(post);
		return postHelper.toDTO(post);
	}
	
	
	
}
