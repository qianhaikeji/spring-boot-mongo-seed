package com.qhkj.seed.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import com.qhkj.seed.api.*;

/**
 * restful 接口注册
 */
@Component
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
    	register(TestRest.class);
    	register(AdminRest.class);
        register(UploadRest.class);
        
        //注册 MultiPart，支持文件上传
        register(MultiPartFeature.class);
    }
}
