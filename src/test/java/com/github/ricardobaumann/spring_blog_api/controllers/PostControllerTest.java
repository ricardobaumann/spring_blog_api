package com.github.ricardobaumann.spring_blog_api.controllers;

import static org.junit.Assert.*;

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
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
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
		
		PostDTO returnPostDTO = new PostDTO(id, category, title, content);
	
		Post post = new Post(category, title, content);
		post.setId(id);
		
		when(postHelper.from(postDTO)).thenReturn(post);
		when(postHelper.toDTO(post)).thenReturn(returnPostDTO);
		when(postRepository.save(Mockito.<Post> any())).thenReturn(post);
		
		mockMvc.perform(post("/posts")
				.content(jsonHelper.objectToString(postDTO))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id", is(id.intValue())));
		
		
		
	}

}
