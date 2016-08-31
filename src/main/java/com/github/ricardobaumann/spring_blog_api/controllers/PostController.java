/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.ricardobaumann.spring_blog_api.dto.FullPostDTO;
import com.github.ricardobaumann.spring_blog_api.dto.PostDTO;
import com.github.ricardobaumann.spring_blog_api.exception.NotFoundException;
import com.github.ricardobaumann.spring_blog_api.exception.UnauthorizedException;
import com.github.ricardobaumann.spring_blog_api.helpers.PostHelper;
import com.github.ricardobaumann.spring_blog_api.models.Post;
import com.github.ricardobaumann.spring_blog_api.services.PostService;

/**
 * Rest controller for posts
 * @author ricardobaumann
 *
 */
@RestController
@RequestMapping(path="/posts")
public class PostController extends BaseController {
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private PostHelper postHelper;
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseStatus(code=HttpStatus.CREATED)
	@ResponseBody
	public PostDTO create(@RequestBody PostDTO postDTO, Principal user) throws UnauthorizedException {
		Post post = postHelper.from(postDTO);
		if (user!=null) {
			post.setUsername(user.getName());
		}
		post = postService.save(post);
		return postHelper.toDTO(post);
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public @ResponseBody List<PostDTO> getPage(
			@RequestParam(name="page", required=false, defaultValue="0") Integer page, 
			@RequestParam(name = "size", required=false, defaultValue="20") Integer size) {
		
		PageRequest pageRequest = new PageRequest(page, size, Direction.DESC, "id");
		return postHelper.toDTOList(postService.findPage(pageRequest));
	}
	
	@RequestMapping(method=RequestMethod.GET, path="users/{user}")
	public @ResponseBody List<PostDTO> getUserPage(
			@RequestParam(name="page", required=false, defaultValue="0") Integer page, 
			@RequestParam(name = "size", required=false, defaultValue="20") Integer size,
			@PathVariable("user") String user) {
		
		PageRequest pageRequest = new PageRequest(page, size, Direction.DESC, "id");
		return postHelper.toDTOList(postService.findByUsername(user, pageRequest));  
	}
	
	@RequestMapping(method=RequestMethod.GET, path = "{id}")
	public @ResponseBody FullPostDTO getFullPost(@PathVariable("id") Long id) throws NotFoundException {
		
		Post post = loadPost(id);
		
		return postHelper.toFullDTO(post);
	}
	
	private Post loadPost(Long id) throws NotFoundException {
		Post post = postService.find(id);
		if (post==null) {
			throw new NotFoundException();
		}
		return post;
	}

	@RequestMapping(method=RequestMethod.DELETE, path = "{id}")
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void remove(@PathVariable("id") Long id, Principal user) throws NotFoundException, UnauthorizedException {
		Post post = loadPost(id);
		postService.delete(post, user);
	}
	
}
