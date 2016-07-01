package com.cx.szsh.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigHelper extends BaseHelper {
	
	@Value("${server.upload.dir}")
	private String uploadDir;

	public String getUploadDir(){
		return this.uploadDir;
	}
}
