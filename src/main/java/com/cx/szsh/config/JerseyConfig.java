package com.cx.szsh.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import com.cx.szsh.api.*;

/**
 * restful 接口注册
 */
@Component
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
    	register(TestRest.class);
    	register(AdminRest.class);
        register(LiveVideoRest.class);
        register(UploadRest.class);
        register(TrafficRest.class);
        register(MedicalRest.class);
        register(ConvenienceRest.class);
        register(TravelRest.class);
        
        //注册 MultiPart，支持文件上传
        register(MultiPartFeature.class);
    }
}
