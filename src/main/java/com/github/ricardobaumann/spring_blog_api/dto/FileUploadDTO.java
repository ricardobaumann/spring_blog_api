/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO to map file uploads on rest controllers
 * @author ricardobaumann
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadDTO {

	private Long id;
	
	private String fileName;
	
}
