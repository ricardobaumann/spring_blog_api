/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.controllers;

import java.io.IOException;

import org.hibernate.metamodel.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.ricardobaumann.spring_blog_api.dto.FileUploadDTO;
import com.github.ricardobaumann.spring_blog_api.exception.NotFoundException;
import com.github.ricardobaumann.spring_blog_api.helpers.PostHelper;
import com.github.ricardobaumann.spring_blog_api.models.Post;
import com.github.ricardobaumann.spring_blog_api.models.PostFile;
import com.github.ricardobaumann.spring_blog_api.services.FileService;
import com.github.ricardobaumann.spring_blog_api.services.PostService;

/**
 * Controller to upload and fetch files associated to posts
 * @author ricardobaumann
 *
 */
@RestController
@RequestMapping(path="posts/{post_id}/files")
public class FileController {

	@Autowired
	private PostHelper postHelper;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private PostService postService;
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseStatus(code=HttpStatus.CREATED)
	public @ResponseBody FileUploadDTO uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("post_id") Long postId) throws IOException {
		if (!file.isEmpty()) {
			Post post = postService.find(postId);
			PostFile postFile = fileService.createFile(file, post);
			return postHelper.toFileDTO(postFile);
		} else {
			throw new ValidationException("file not found on request body");
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "{file_id}")
	public @ResponseBody ResponseEntity<?> getFile(@PathVariable("file_id") Long fileId) throws NotFoundException {
		ResponseEntity<Resource> response = ResponseEntity.ok(fileService.getFile(fileId));
		if (response!=null) {
			return response;
		} else {
			throw new NotFoundException();
		}
	}
	
	
}
