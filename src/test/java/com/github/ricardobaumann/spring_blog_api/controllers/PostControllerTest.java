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
import com.github.ricardobaumann.spring_blog_api.dto.PostDTO;
import com.github.ricardobaumann.spring_blog_api.helpers.PostHelper;
import com.github.ricardobaumann.spring_blog_api.models.Post;
import com.github.ricardobaumann.spring_blog_api.repositories.PostRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, TestContext.class})
@WebAppConfiguration
public class PostControllerTest {
	
	@Mock
	private PostRepository postRepository;
	
	@Spy
	private PostHelper postHelper;
	
	@InjectMocks
	private PostController postController;
	
	@Autowired
	WebApplicationContext context;

	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	private MockMvc mockMvc;
	
	@Autowired
	private JsonHelper jsonHelper;

	private String accessToken;

	private MockMvc authMockMvc;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
		authMockMvc = MockMvcBuilders.webAppContextSetup(context)
				.addFilter(springSecurityFilterChain)
				.build();
		accessToken = getAccessToken("craig", "spring");
	}
	
	private String getAccessToken(String username, String password) throws Exception {
		String authorization = "Basic " + new String(Base64Utils.encode("clientapp:123456".getBytes()));
		String contentType = MediaType.APPLICATION_JSON + ";charset=UTF-8";

		// @formatter:off
		String content = authMockMvc
				.perform(
						post("/oauth/token")
								.header("Authorization", authorization)
								.contentType(
										MediaType.APPLICATION_FORM_URLENCODED)
								.param("username", username)
								.param("password", password)
								.param("grant_type", "password")
								.param("scope", "read write")
								.param("client_id", "clientapp")
								.param("client_secret", "123456"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.access_token", is(notNullValue())))
				.andExpect(jsonPath("$.token_type", is(equalTo("bearer"))))
				.andExpect(jsonPath("$.refresh_token", is(notNullValue())))
				.andExpect(jsonPath("$.expires_in", is(greaterThan(4000))))
				.andExpect(jsonPath("$.scope", is(equalTo("read write"))))
				.andReturn().getResponse().getContentAsString();

		// @formatter:on

		return content.substring(17, 53);
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
				.header("Authorization", String.format("Bearer %s", accessToken))
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
