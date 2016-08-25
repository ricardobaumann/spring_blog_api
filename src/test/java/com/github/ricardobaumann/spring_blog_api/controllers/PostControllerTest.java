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
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import static org.hamcrest.Matchers.is;

import com.github.ricardobaumann.spring_blog_api.Application;
import com.github.ricardobaumann.spring_blog_api.dto.PostDTO;
import com.github.ricardobaumann.spring_blog_api.helpers.PostHelper;
import com.github.ricardobaumann.spring_blog_api.models.Post;
import com.github.ricardobaumann.spring_blog_api.repositories.PostRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, TestContext.class})
public class PostControllerTest {
	
	@Mock
	private PostRepository postRepository;
	
	@Spy
	private PostHelper postHelper;
	
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
		
		when(postRepository.save(Mockito.<Post> any())).thenReturn(post);
		
		mockMvc.perform(post("/posts")
				.content(jsonHelper.objectToString(postDTO))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id", is(id.intValue())));
		
	}
	
	@Test
	public void testCreateValidationErrors() throws Exception {
		
		String message = "error";
		ValidationException validationException = new ValidationException(new ValidationException(message));
		
		PostDTO postDTO = new PostDTO();
		when(postRepository.save(Mockito.<Post> any())).thenThrow(validationException);
		
		mockMvc.perform(post("/posts")
				.content(jsonHelper.objectToString(postDTO))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isUnprocessableEntity())
		.andExpect(jsonPath("$.message", is(message)));
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetEmptyPage() throws Exception {
		Page<Post> postPage = mock(Page.class);
		when(postRepository.findAll(Mockito.<PageRequest> any())).thenReturn(postPage);
		
		mockMvc.perform(get("/posts")).andExpect(status().isOk()).andExpect(content().string("[]"));
	}
	
	@Test
	public void testGetFilledPage() throws Exception {
		Page<Post> postPage = mock(Page.class);
		String title = "title";
		String category = "category";
		String content = "content";
		List<Post> postList = Arrays.asList(new Post(category, title, content));
		when(postPage.getContent()).thenReturn(postList);
		when(postRepository.findAll(Mockito.<PageRequest> any())).thenReturn(postPage);
		
		mockMvc.perform(get("/posts")).andExpect(status().isOk())
		.andExpect(content().string(is(jsonHelper.objectToString(postList))));
	}

}