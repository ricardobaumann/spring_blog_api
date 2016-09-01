package com.github.ricardobaumann.spring_blog_api.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.ricardobaumann.spring_blog_api.Config;
import com.github.ricardobaumann.spring_blog_api.models.Post;
import com.github.ricardobaumann.spring_blog_api.models.PostFile;
import com.github.ricardobaumann.spring_blog_api.repositories.PostFileRepository;

/**
 * Service class to handle posts files
 * @author ricardobaumann
 *
 */
@Service
public class FileService {
	
	@Autowired
	private Config config;
	
	@Autowired
	private PostFileRepository postFileRepository;
	
	@Autowired
	private ResourceLoader resourceLoader;

	public PostFile createFile(MultipartFile file, Post post) throws IOException {
		
		PostFile postFile = postFileRepository.save(new PostFile(file.getOriginalFilename(), post));
		String path = getPostFileDir(postFile);
		File fileDir = new File(path);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		copyFile(file, path);
		
		return postFile;
		
	}

	protected void copyFile(MultipartFile file, String path) throws IOException {
		Files.copy(file.getInputStream(), Paths.get(path, file.getOriginalFilename()));
		
	}

	protected String getPostFileDir(PostFile postFile) {
		return config.getRootUploadFilePath()+File.separator+postFile.getId();
	}

	public Resource getFile(Long fileId) {
		PostFile postFile = postFileRepository.findOne(fileId);
		if (postFile!=null) {
			return resourceLoader.getResource("file:" + getPostFilePath(postFile).toString());
		} else {
			return null;
		}
	}

	protected String getPostFilePath(PostFile postFile) {
		return getPostFileDir(postFile)+File.separator+postFile.getFileName();
	}

	
	
}
