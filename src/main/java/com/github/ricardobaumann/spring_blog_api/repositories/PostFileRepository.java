/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.github.ricardobaumann.spring_blog_api.models.PostFile;

/**
 * Repository for post files entity
 * @author ricardobaumann
 *
 */
@Repository
public interface PostFileRepository extends PagingAndSortingRepository<PostFile, Long> {

}
