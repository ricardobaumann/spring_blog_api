/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.github.ricardobaumann.spring_blog_api.models.Post;

/**
 * Repository for post data
 * @author ricardobaumann
 *
 */
@Repository
public interface PostRepository extends CrudRepository<Post, Long> {
	
}
