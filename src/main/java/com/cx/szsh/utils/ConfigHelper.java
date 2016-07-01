package com.cx.szsh.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigHelper extends BaseHelper {
	@Value("${server.machineId}")
	private int machineId;
	
	@Value("${server.upload.dir}")
	private String uploadDir;
	
	public short getMachineId(){
		return (short)(this.machineId & 0xf);
	}
	
	public String getUploadDir(){
		return this.uploadDir;
	}
}
