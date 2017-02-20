package com.github.ricardobaumann.spring_blog_api.repositories;

import com.github.ricardobaumann.spring_blog_api.Application;
import com.github.ricardobaumann.spring_blog_api.models.Comment;
import com.github.ricardobaumann.spring_blog_api.models.Post;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.validation.ConstraintViolationException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
	  TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
	@RunWith(SpringJUnit4ClassRunner.class)
	@SpringBootTest(classes = Application.class)
	@DatabaseSetup(value = { PostRepositoryTest.DATASET })
	@DatabaseTearDown(type = DatabaseOperation.DELETE, value = { PostRepositoryTest.DATASET })
	@DirtiesContext
public class CommentRepositoryTest {
	
	protected static final String DATASET = "classpath:datasets/comments.xml";
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
	}
	
	@After
	public void after() {
		commentRepository.deleteAll();
	}
	
	@Test
	public void testSaveSuccesfully() {
		Post post = postRepository.findAll().iterator().next();
		String content = "content";
		String username = "username";
		Comment comment = new Comment(username, content);
		comment.setPost(post);
		comment = commentRepository.save(comment);
		assertThat(comment.getId(), is(1L));
	}

	@Test
	public void testFieldValidation() throws IllegalAccessException, InvocationTargetException {
		Post post = postRepository.findAll().iterator().next();
		String content = "content";
		String username = "username";
		for (String field : Arrays.asList("content", "username", "post")) {
			Comment comment = new Comment(username, content);
			comment.setPost(post);
			BeanUtils.setProperty(comment, field, null);
			expectedException.expect(ConstraintViolationException.class);
			
			commentRepository.save(comment);
		}
	}

}
