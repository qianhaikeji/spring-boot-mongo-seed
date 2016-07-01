package com.cx.szsh.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cx.szsh.utils.ConfigHelper;
import com.cx.szsh.utils.DateHelper;
import com.cx.szsh.utils.QiNiuHelper;
import com.cx.szsh.utils.RestfulHelper;

@Component
@Path("/upload")
public class UploadRest extends BaseRest {

	@Autowired
	private QiNiuHelper qiNiuHelper;
	
	@Autowired
	private ConfigHelper configHelper;

	@POST
	@Path("/image")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public Response uploadFile(@Context HttpServletRequest request, @FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition contentDispositionHeader) {
		String fileName = contentDispositionHeader.getFileName();
		String suffix = fileName.trim().substring(fileName.lastIndexOf(".")+1);
		fileName = UUID.randomUUID().toString() + "." + suffix;
		
		try {
			Map<Object, Object> apiRsp = new HashMap<Object, Object>();
			// String url = uploadFileQiNiu(fileName, fileInputStream);
			
			String url = uploadFileLocal(fileName, fileInputStream);
			apiRsp.put("url", url);
			return Response.status(Response.Status.OK).entity(apiRsp).build();
		} catch (IOException e) {
			logger.error(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage())).build();
		} finally {
			try {
				fileInputStream.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}

	private String uploadFileLocal(String fileName, InputStream fileInputStream) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat(DateHelper.FORMAT_DATE);
		String filepath = Paths.get(configHelper.getUploadDir(), "image", sdf.format(new Date()), fileName).toString();
		File file = new File(filepath);
		File parent = file.getParentFile();
		// 判断目录是否存在，不在创建
		if (parent != null && !parent.exists()) {
			parent.mkdirs();
		}
		file.createNewFile();

		OutputStream outpuStream = new FileOutputStream(file);
		int read = 0;
		byte[] bytes = new byte[1024];

		while ((read = fileInputStream.read(bytes)) != -1) {
			outpuStream.write(bytes, 0, read);
		}

		outpuStream.flush();
		outpuStream.close();
		
		return Paths.get("/upload", "image", sdf.format(new Date()), fileName).toString();
	}

	private String uploadFileQiNiu(String fileName, InputStream inputStream) throws IOException {
		qiNiuHelper.upload(fileName, inputStream);
		return qiNiuHelper.getFileUrl(fileName);
	}

}
