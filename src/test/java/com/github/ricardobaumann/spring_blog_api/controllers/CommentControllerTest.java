/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.controllers;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.github.ricardobaumann.spring_blog_api.Application;
import com.github.ricardobaumann.spring_blog_api.dto.CommentDTO;
import com.github.ricardobaumann.spring_blog_api.dto.PostDTO;
import com.github.ricardobaumann.spring_blog_api.helpers.PostHelper;
import com.github.ricardobaumann.spring_blog_api.models.Comment;
import com.github.ricardobaumann.spring_blog_api.models.Post;
import com.github.ricardobaumann.spring_blog_api.repositories.CommentRepository;
import com.github.ricardobaumann.spring_blog_api.repositories.PostRepository;
import com.github.ricardobaumann.spring_blog_api.services.CommentService;
import com.github.ricardobaumann.spring_blog_api.services.PostService;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

/**
 * @author ricardobaumann
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, TestContext.class})
public class CommentControllerTest {

	@Autowired
	private JsonHelper jsonHelper;
	
	@Mock
	private CommentService commentService;
	
	@Mock
	private PostService postService;
	
	@Spy
	private PostHelper postHelper;
	
	@InjectMocks
	private CommentController commentController;

	private MockMvc mockMvc;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
	}
	
	@Test
	public void testCreateValidationError() throws Exception {
		CommentDTO commentDTO = new CommentDTO();
		when(postService.find(Mockito.<Long> any())).thenReturn(null);
		when(commentService.save(Mockito.<Comment> any())).thenThrow(new ValidationException());
		
		mockMvc.perform(post("/posts/{post_id}/comments", 1 )
				.content(jsonHelper.objectToString(commentDTO))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isUnprocessableEntity());
		
		verify(postService).find(Mockito.<Long> any());
		verify(commentService).save(Mockito.<Comment> any());
	}

	@Test
	public void testRemoveSuccesfully() throws Exception {
		Post post = new Post();
		when(postService.find(Mockito.<Long> any())).thenReturn(post);
		Comment comment = new Comment();
		comment.setPost(post);
		when(commentService.find(Mockito.<Post> any(), Mockito.<Long> any())).thenReturn(comment);
		doNothing().when(commentService).delete(Mockito.<Comment> any(), Mockito.<Principal> any());
		
		mockMvc.perform(delete("/posts/{post_id}/comments/{comment_id}", 1,1))
		.andExpect(status().isNoContent());
		
		verify(postService).find(Mockito.<Long> any());
		verify(commentService).find(Mockito.<Post> any(), Mockito.<Long> any());
		verify(commentService).delete(Mockito.<Comment> any(), Mockito.<Principal> any());
	}
	
	@Test
	public void testRemoveInexistentComment() throws Exception {
		Post post = new Post();
		when(postService.find(Mockito.<Long> any())).thenReturn(post);
		Comment comment = new Comment();
		comment.setPost(post);
		when(commentService.find(Mockito.<Post> any(), Mockito.<Long> any())).thenReturn(null);
		//doNothing().when(commentService).delete(Mockito.<Comment> any(), Mockito.<Principal> any());
		
		mockMvc.perform(delete("/posts/{post_id}/comments/{comment_id}", 1,1))
		.andExpect(status().isNotFound());
		
		verify(postService).find(Mockito.<Long> any());
		verify(commentService).find(Mockito.<Post> any(), Mockito.<Long> any());
		//verify(commentService).delete(Mockito.<Comment> any(), Mockito.<Principal> any());
	}
	
	@Test
	public void testRemoveCommentFromInexistentPost() throws Exception {
		Post post = new Post();
		when(postService.find(Mockito.<Long> any())).thenReturn(null);
		Comment comment = new Comment();
		comment.setPost(post);
		//when(commentService.find(Mockito.<Post> any(), Mockito.<Long> any())).thenReturn(comment);
		//doNothing().when(commentService).delete(Mockito.<Comment> any(), Mockito.<Principal> any());
		
		mockMvc.perform(delete("/posts/{post_id}/comments/{comment_id}", 1,1))
		.andExpect(status().isNotFound());
		
		verify(postService).find(Mockito.<Long> any());
		//verify(commentService).find(Mockito.<Post> any(), Mockito.<Long> any());
		//verify(commentService).delete(Mockito.<Comment> any(), Mockito.<Principal> any());
	}
	
	@Test
	public void testCreateSucessfully() throws Exception {
		
		String content = "content";
		
		String username = "user";
		Long commentId = 1L;
		
		Long postId = 1L;
		Post post = new Post();
		post.setId(postId);
		
		CommentDTO commentDTO = new CommentDTO(username,content);
		
		Comment comment = new Comment(username , content);
		comment.setId(commentId);
		
		when(postService.find(Mockito.<Long> any())).thenReturn(post);
		
		when(commentService.save(Mockito.<Comment> any())).thenReturn(comment);
		
		mockMvc.perform(post("/posts/{post_id}/comments", postId )
				.content(jsonHelper.objectToString(commentDTO))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id", is(commentId.intValue())));
		
		verify(postService).find(Mockito.<Long> any());
		verify(commentService).save(Mockito.<Comment> any());
		
	}

}
