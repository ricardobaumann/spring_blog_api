/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.repositories;

import com.github.ricardobaumann.spring_blog_api.models.Post;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.github.ricardobaumann.spring_blog_api.models.Comment;

import java.util.List;

/**
 * Respotory to fetch and persist comment data
 * @author ricardobaumann
 *
 */
public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {

    List<Comment> findByPost(Post post);
}
