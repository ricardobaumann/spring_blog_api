package com.github.ricardobaumann.spring_blog_api.repositories;

import static org.junit.Assert.*;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.github.ricardobaumann.spring_blog_api.Application;
import com.github.ricardobaumann.spring_blog_api.models.Post;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import antlr.CharBuffer;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsArrayContaining.*;

@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
	  TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
	@RunWith(SpringJUnit4ClassRunner.class)
	@SpringApplicationConfiguration(classes = Application.class)
	@DatabaseSetup(PostRepositoryTest.DATASET)
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { PostRepositoryTest.DATASET })
	@DirtiesContext
public class PostRepositoryTest {
	
	protected static final String DATASET = "classpath:datasets/posts.xml";
	
	@Autowired
	private PostRepository postRepository;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testFindByUsername() {
		for (int x=1;x<=2;x++) {
			Page<Post> posts = postRepository.findByUsername("user1", new PageRequest(x-1, 1));
			assertThat(posts.getContent(), hasSize(1));
			assertThat(posts.getContent(), contains(hasProperty("title", is("title "+x))));
			assertThat(posts.getContent(), contains(hasProperty("content", is("content "+x))));
			assertThat(posts.getContent(), contains(hasProperty("username", is("user1"))));
		}
	}
	
	@Test
	public void testSaveSuccesfully() {
		String username = "user";
		String content = "content";
		String category = "category";
		String title = "title";
		Post post = new Post(category, title, content, username);
		post = postRepository.save(post);
		assertThat(post.getId(), is(4L));
	}
	
	@Test
	public void testFieldNullValidation() throws IllegalAccessException, InvocationTargetException {
		
		String username = "user";
		String content = "content";
		String category = "category";
		String title = "title";
		
		for (String field : Arrays.asList("title", "content", "category", "username")) {
			Post post = new Post(category, title, content, username);
			
			expectedException.expect(ConstraintViolationException.class);
			
			BeanUtils.setProperty(post, field, null);
			postRepository.save(post);
			
		};
		
	}
	
	@Test
	public void testFieldSizeValidation() throws IllegalAccessException, InvocationTargetException {
		
		String username = "user";
		String content = "content";
		String category = "category";
		String title = "title";
		
		Map<String, Integer> sizes = new HashMap<>();
		sizes.put("content", 1000);
		sizes.put("title", 100);
		sizes.put("category", 50);
		sizes.put("username", 50);
		
		for (String field : Arrays.asList("title", "content", "category", "username")) {
			Post post = new Post(category, title, content, username);
	
			expectedException.expect(ConstraintViolationException.class);
			
			BeanUtils.setProperty(post, field, java.nio.CharBuffer.allocate( sizes.get(field)+1 ).toString().replace( '\0', ' ' ));
			postRepository.save(post);
			
		}
		
		
	}
	

}
