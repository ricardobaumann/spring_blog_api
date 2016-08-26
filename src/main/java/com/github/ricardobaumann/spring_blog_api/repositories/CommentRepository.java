/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.github.ricardobaumann.spring_blog_api.models.Comment;

/**
 * @author ricardobaumann
 *
 */
public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {

}
