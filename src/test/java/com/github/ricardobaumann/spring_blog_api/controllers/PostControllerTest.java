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
import com.github.ricardobaumann.spring_blog_api.dto.FullPostDTO;
import com.github.ricardobaumann.spring_blog_api.dto.PostDTO;
import com.github.ricardobaumann.spring_blog_api.helpers.PostHelper;
import com.github.ricardobaumann.spring_blog_api.models.Comment;
import com.github.ricardobaumann.spring_blog_api.models.Post;
import com.github.ricardobaumann.spring_blog_api.repositories.PostRepository;
import com.github.ricardobaumann.spring_blog_api.services.PostService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, TestContext.class})
public class PostControllerTest {
	
	@Spy
	private PostHelper postHelper;
	
	@Mock
	private PostService postService;
	
	@InjectMocks
	private PostController postController;
	
	private MockMvc mockMvc;
	
	@Autowired
	private JsonHelper jsonHelper;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
	}


	@Test
	public void testCreatePostSuccesfully() throws Exception {
		
		String title = "title";
		String category = "category";
		String content = "content";
		Long id = 1L;
		
		PostDTO postDTO = new PostDTO(category, title, content);
	
		Post post = new Post(category, title, content);
		post.setId(id);
		
		when(postService.save(Mockito.<Post> any())).thenReturn(post);
		
		mockMvc.perform(post("/posts")
				.content(jsonHelper.objectToString(postDTO))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id", is(id.intValue())));
		
		verify(postService).save(Mockito.<Post> any());
		
	}
	
	@Test
	public void testCreateValidationErrors() throws Exception {
		
		String message = "error";
		ValidationException validationException = new ValidationException(new ValidationException(message));
		
		PostDTO postDTO = new PostDTO();
		when(postService.save(Mockito.<Post> any())).thenThrow(validationException);
		
		mockMvc.perform(post("/posts")
				.content(jsonHelper.objectToString(postDTO))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isUnprocessableEntity())
		.andExpect(jsonPath("$.message", is(message)));
		
		verify(postService).save(Mockito.<Post> any());
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetEmptyPage() throws Exception {
		Page<Post> postPage = mock(Page.class);
		when(postService.findPage(Mockito.<PageRequest> any())).thenReturn(postPage);
		
		mockMvc.perform(get("/posts")).andExpect(status().isOk()).andExpect(content().string("[]"));
		
		verify(postService).findPage(Mockito.<PageRequest> any());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetFilledPage() throws Exception {
		Page<Post> postPage = mock(Page.class);
		String title = "title";
		String category = "category";
		String content = "content";
		List<Post> postList = Arrays.asList(new Post(category, title, content));
		when(postPage.getContent()).thenReturn(postList);
		when(postService.findPage(Mockito.<PageRequest> any())).thenReturn(postPage);
		
		mockMvc.perform(get("/posts")).andExpect(status().isOk())
		.andExpect(content().string(is(jsonHelper.objectToString(postList))));
		
		verify(postPage).getContent();
		verify(postService).findPage(Mockito.<PageRequest> any());
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetUserPage() throws Exception {
		Page<Post> postPage = mock(Page.class);
		String title = "title";
		String category = "category";
		String content = "content";
		List<Post> postList = Arrays.asList(new Post(category, title, content));
		when(postPage.getContent()).thenReturn(postList);
		when(postService.findByUsername(Mockito.<String> any(), Mockito.<PageRequest> any())).thenReturn(postPage);
		
		mockMvc.perform(get("/posts/users/user")).andExpect(status().isOk())
		.andExpect(content().string(is(jsonHelper.objectToString(postList))));
		
		verify(postPage).getContent();
		verify(postService).findByUsername(Mockito.<String> any(), Mockito.<PageRequest> any());
	}
	
	@Test
	public void testGetPostByIDSuccesfully() throws Exception {
		String title = "title";
		String category = "category";
		String content = "content";
		String username = "user";
		String commentContent = "comment";
		
		Post post = new Post(category, title, content);
		post.setComments(Arrays.asList(new Comment(username, commentContent)));
		
		when(postService.find(Mockito.<Long> any())).thenReturn(post);
		
		mockMvc.perform(get("/posts/1")).andExpect(status().isOk())
		.andExpect(jsonPath("$.title", is(title)))
		.andExpect(jsonPath("$.content", is(content)))
		.andExpect(jsonPath("$.category", is(category)))
		.andExpect(jsonPath("$.comments[0].username", is(username)))
		.andExpect(jsonPath("$.comments[0].content", is(commentContent)));
		
		verify(postService).find(Mockito.<Long> any());
		
	}
	
	@Test
	public void testGetInexistentPostByID() throws Exception {
		when(postService.find(Mockito.<Long> any())).thenReturn(null);
		
		mockMvc.perform(get("/posts/1")).andExpect(status().isNotFound());
		
		verify(postService).find(Mockito.<Long> any());
	}
	
	@Test
	public void testDeleteExistentPostByID() throws Exception {
		String title = "title";
		String category = "category";
		String content = "content";
		
		Post post = new Post(category, title, content);
		when(postService.find(Mockito.<Long> any())).thenReturn(post);
		doNothing().when(postService).delete(Mockito.<Post> any(), Mockito.<Principal> any());
		
		mockMvc.perform(delete("/posts/1")).andExpect(status().isNoContent());
		
		verify(postService).find(Mockito.<Long> any());
		verify(postService).delete(Mockito.<Post> any(), Mockito.<Principal> any());
	}
	
	@Test
	public void testDeleteInexistentPostByID() throws Exception {
		when(postService.find(Mockito.<Long> any())).thenReturn(null);
		mockMvc.perform(delete("/posts/1")).andExpect(status().isNotFound());
		verify(postService).find(Mockito.<Long> any());
	}

}
