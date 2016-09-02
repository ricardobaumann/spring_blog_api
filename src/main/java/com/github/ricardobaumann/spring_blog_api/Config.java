package com.github.ricardobaumann.spring_blog_api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@PropertySource("classpath:application.properties")
public class Config {

	@Value("${root_upload_file_path}")
	private String rootUploadFilePath;
	
}
