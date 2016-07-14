package com.qhkj.seed.utils;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

import org.apache.commons.io.IOUtils; 

@Component
public class QiNiuHelper {
	
	@Value("${qiniu.accessKey}")
	private String accessKey;

	@Value("${qiniu.secretKey}")
	private String secretKey;

	@Value("${qiniu.bucketName}")
	private String bucketName;
	
	@Value("${qiniu.bucketHost}")
	private String bucketHost;
	
	private static UploadManager uploadManager = new UploadManager();
	
	//简单上传，使用默认策略，只需要设置上传的空间名就可以了
	private String getUpToken(){
		Auth auth = Auth.create(accessKey, secretKey);
		return auth.uploadToken(bucketName);
	}

	public void upload(String fileName, InputStream inputStream) throws IOException {
		byte[] bytes = IOUtils.toByteArray(inputStream);
		uploadManager.put(bytes, fileName, getUpToken());
	}
	
	public String getFileUrl(String fileName) {
		return bucketHost + "/" + fileName;
	}

}
