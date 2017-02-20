package com.github.ricardobaumann.spring_blog_api.services;

import com.github.ricardobaumann.spring_blog_api.Application;
import com.github.ricardobaumann.spring_blog_api.exception.UnauthorizedException;
import com.github.ricardobaumann.spring_blog_api.models.Comment;
import com.github.ricardobaumann.spring_blog_api.models.Post;
import com.github.ricardobaumann.spring_blog_api.repositories.CommentRepository;
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
public class CommentServiceTest {

	@Mock
	private CommentRepository commentRepository;
	
	@InjectMocks
	private CommentService commentService;
	
	@Mock
	private Principal user;

	private String username = "username";
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(user.getName()).thenReturn(username );
	}

	@Test
	public void testDeleteSuccesfully() throws UnauthorizedException {
		
		Post post = new Post();
		post.setUsername(username);
		
		Comment comment = new Comment();
		comment.setPost(post);
		
		doNothing().when(commentRepository).delete(comment);
		
		commentService.delete(comment, user);
		
		verify(commentRepository).delete(comment);
		
	}
	
	@Test
	public void testDeleteCommentFromOtherUserPost() throws UnauthorizedException {
		Post post = new Post();
		post.setUsername("other");
		
		Comment comment = new Comment();
		comment.setPost(post);
		
		expectedException.expect(UnauthorizedException.class);
		
		commentService.delete(comment, user);
	}

}
