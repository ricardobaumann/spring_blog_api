/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.controllers;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;

import javax.validation.ValidationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.github.ricardobaumann.spring_blog_api.Application;
import com.github.ricardobaumann.spring_blog_api.dto.CommentDTO;
import com.github.ricardobaumann.spring_blog_api.helpers.PostHelper;
import com.github.ricardobaumann.spring_blog_api.models.Comment;
import com.github.ricardobaumann.spring_blog_api.models.Post;
import com.github.ricardobaumann.spring_blog_api.services.CommentService;
import com.github.ricardobaumann.spring_blog_api.services.PostService;

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
	
	@Autowired
	private ExceptionMapper exceptionMapper;

	private MockMvc mockMvc;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(commentController).setControllerAdvice(exceptionMapper).build();
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
