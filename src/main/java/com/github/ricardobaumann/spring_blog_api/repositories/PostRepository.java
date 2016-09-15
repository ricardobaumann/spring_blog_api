/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.repositories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.github.ricardobaumann.spring_blog_api.models.Post;

/**
 * Repository to fetch and persist post data
 * @author ricardobaumann
 *
 */
@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
	
	public Page<Post> findByUsername(String username, Pageable pageable);
	
}
