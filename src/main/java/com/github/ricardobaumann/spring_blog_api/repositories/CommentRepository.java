/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.github.ricardobaumann.spring_blog_api.models.Comment;

/**
 * Respotory to fetch and persist comment data
 * @author ricardobaumann
 *
 */
public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {

}
