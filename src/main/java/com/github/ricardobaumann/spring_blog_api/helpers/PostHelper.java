/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.helpers;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.github.ricardobaumann.spring_blog_api.dto.CommentDTO;
import com.github.ricardobaumann.spring_blog_api.dto.FileUploadDTO;
import com.github.ricardobaumann.spring_blog_api.dto.FullPostDTO;
import com.github.ricardobaumann.spring_blog_api.dto.PostDTO;
import com.github.ricardobaumann.spring_blog_api.models.Comment;
import com.github.ricardobaumann.spring_blog_api.models.Post;
import com.github.ricardobaumann.spring_blog_api.models.PostFile;

/**
 * Helper for the rest controllers
 * @author ricardobaumann
 *
 */
@Component
public class PostHelper {
	

	public Post from(PostDTO postDTO) {
		return new Post(postDTO.getCategory(),postDTO.getTitle(),postDTO.getContent());
	}

	public PostDTO toDTO(Post post) {
		return new PostDTO(post.getId(),post.getCategory(), post.getTitle(), post.getContent());
	}

	public List<PostDTO> toDTOList(Page<Post> page) {
		return page.getContent().stream().map(post -> toDTO(post)).collect(Collectors.toList());
	}

	public FullPostDTO toFullDTO(Post post) {
		FullPostDTO fullPostDTO = new FullPostDTO(post.getId(), post.getCategory(), post.getTitle(), post.getContent());
		List<Comment> comments = post.getComments();
		if (comments!=null) {
			fullPostDTO.setComments(comments.stream()
					.map(comment -> toCommentDTO(comment)).collect(Collectors.toList()));
		}
		return fullPostDTO;
	}

	public CommentDTO toCommentDTO(Comment comment) {
		return new CommentDTO(comment.getId(),comment.getUsername(), comment.getContent());
	}
	
	public Comment fromCommentDto(CommentDTO commentDTO) {
		return new Comment(commentDTO.getUsername(), commentDTO.getContent());
	}

	public FileUploadDTO toFileDTO(PostFile postFile) {
		return new FileUploadDTO(postFile.getId(), postFile.getFileName());
	}
	
}
