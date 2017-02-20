package com.github.ricardobaumann.spring_blog_api.services;

import com.github.ricardobaumann.spring_blog_api.Application;
import com.github.ricardobaumann.spring_blog_api.exception.UnauthorizedException;
import com.github.ricardobaumann.spring_blog_api.models.Post;
import com.github.ricardobaumann.spring_blog_api.repositories.PostRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.security.Principal;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class, TestContext.class})
public class PostServiceTest {

	@Mock
	private PostRepository postRepository;
	
	@Mock
	private Principal user;
	
	@InjectMocks
	private PostService postService;

	private String username = "username";
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(user.getName()).thenReturn(username);
	}

	@Test
	public void testDeleteSuccesfully() throws UnauthorizedException {
		Post post = new Post("category", "category","content");
		post.setUsername(username);
		doNothing().when(postRepository).delete(post);
		postService.delete(post, user);
		verify(postRepository).delete(post);
		
	}
	
	@Test
	public void testDeletePostFromOtherUser() throws UnauthorizedException {
		Post post = new Post("category", "category","content");
		post.setUsername("another");
		expectedException.expect(UnauthorizedException.class);
		postService.delete(post, user);
		
	}

}
