/**
 * 
 */
package com.github.ricardobaumann.spring_blog_api.dto;

/**
 * DTO to map file uploads on rest controllers
 * @author ricardobaumann
 *
 */
public class FileUploadDTO {

	private Long id;
	
	private String fileName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public FileUploadDTO(Long id, String fileName) {
		super();
		this.id = id;
		this.fileName = fileName;
	}

	public FileUploadDTO() {
		super();
	}
	
	
	
}
