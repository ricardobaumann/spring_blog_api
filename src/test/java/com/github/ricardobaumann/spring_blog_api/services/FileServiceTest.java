package com.github.ricardobaumann.spring_blog_api.services;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import com.github.ricardobaumann.spring_blog_api.Application;
import com.github.ricardobaumann.spring_blog_api.Config;
import com.github.ricardobaumann.spring_blog_api.models.Post;
import com.github.ricardobaumann.spring_blog_api.models.PostFile;
import com.github.ricardobaumann.spring_blog_api.repositories.PostFileRepository;

/**
 * @author ricardobaumann
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, TestContext.class})
public class FileServiceTest {
	
	@Mock
	private PostFileRepository postFileRepository;
	
	@Mock
	private ResourceLoader resourceLoader;
	
	@Mock
	private Config config;
	
	@Spy
	@InjectMocks
	private FileService fileService;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testCreateFileSucessfully() throws IOException {
		
		String fileName = "filename";
		Post post = new Post();
		PostFile postFile = new PostFile(fileName, post);
		when(postFileRepository.save(Mockito.<PostFile> any())).thenReturn(postFile);
		
		MultipartFile file = Mockito.mock(MultipartFile.class);
		String path = fileService.getPostFileDir(postFile);
		
		doNothing().when(fileService).copyFile(file, path);
		
		PostFile returnPostFile = fileService.createFile(file, post);
		
		assertThat(returnPostFile, is(postFile));
		
		verify(postFileRepository).save(Mockito.<PostFile> any());
		verify(fileService).copyFile(file, path);
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCreateValidationError() throws IOException {
		
		String fileName = "filename";
		Post post = new Post();
		PostFile postFile = new PostFile(fileName, post);
		when(postFileRepository.save(Mockito.<PostFile> any())).thenThrow(new ConstraintViolationException(Collections.EMPTY_SET));
		
		MultipartFile file = Mockito.mock(MultipartFile.class);
		
		expectedException.expect(ConstraintViolationException.class);
		
		PostFile returnPostFile = fileService.createFile(file, post);
		
		assertThat(returnPostFile, is(postFile));
		
		verify(postFileRepository).save(Mockito.<PostFile> any());
		
	}

	@Test
	public void testGetExistentFile() {
	
		String fileName = "filename";
		Post post = new Post();
		Long fileId = 1L;
		PostFile postFile = new PostFile(fileName, post);
		
		when(postFileRepository.findOne(fileId)).thenReturn(postFile);
		when(config.getRootUploadFilePath()).thenReturn("/tmp/");
		Resource resource = Mockito.mock(Resource.class);
		when(resourceLoader.getResource(Mockito.<String> any())).thenReturn(resource);
		
		Resource returnedResource = fileService.getFile(fileId);
		
		assertThat(returnedResource, is(notNullValue()));
		
	}
	
	@Test
	public void getInexistentFile() {
		
		String fileName = "filename";
		Post post = new Post();
		Long fileId = 1L;
		PostFile postFile = new PostFile(fileName, post);
		
		when(postFileRepository.findOne(fileId)).thenReturn(postFile);
		
		when(resourceLoader.getResource(Mockito.<String> any())).thenReturn(null);
		
		Resource returnedResource = fileService.getFile(fileId);
		
		assertThat(returnedResource, is(nullValue()));
		
		verify(postFileRepository).findOne(fileId);
	}

}
