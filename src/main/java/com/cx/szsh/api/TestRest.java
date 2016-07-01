package com.cx.szsh.api;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cx.szsh.models.Test;
import com.cx.szsh.services.TestService;
import com.cx.szsh.utils.HttpClientHelper;

@Component
@Path("/test")
public class TestRest extends BaseRest {
    @Autowired
    private TestService testService;

    @GET
    @Path("/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response json() {    	
    	Map<Object, Object> apiResponse = new HashMap<Object, Object>();
    	apiResponse.put("apiresponse", "test");
        return Response.ok(apiResponse).build();
    }
    
    @GET
    @Path("/httpclient")
    @Produces(MediaType.APPLICATION_JSON)
    public Response httpclient() {
    	Map<String, Object> reqParams = new HashMap<String, Object>();
    	Map<Object, Object> apiResponse = new HashMap<Object, Object>();
    	HttpClientHelper hc = new HttpClientHelper();
    	reqParams.put("type", "customer");
    	reqParams.put("limit", 10);
    	reqParams.put("offset", 0);
    	apiResponse.put("data", hc.get("https://yuanfenba.leanapp.cn/api/users", reqParams));
        return Response.ok(apiResponse).build();
    }
    
    @GET
    @Path("/redis")
    @Produces(MediaType.APPLICATION_JSON)
    public Response mongo() {
    	Map<Object, Object> apiResponse = new HashMap<Object, Object>();
        Test test = testService.find("1");
        apiResponse.put("message", test.toString());
        return Response.ok(apiResponse).build();
    }
    
//    @GET
//    @Path("/bean2Json")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response bean2Json() {
//        Map<Object, Object> apiResponse = new HashMap<Object, Object>();
//        apiResponse.put("apiresponse", "test");
//        apiResponse.put("caohongtao", "dashuaige");
//        
//        Map<Object, Object> wife = new HashMap<Object, Object>();
//        wife.put("sex", "ss");
//        wife.put("name", "kathy");
//        apiResponse.put("wife", wife);
//        
//        Map<Object, Object> data = new HashMap<Object, Object>();
//        data.put("Bean2Json", new Bean2Json("Bean2Json"));
//        apiResponse.put("data", new Bean2Json("xxxxx"));
//        
//        return Response.status(200).entity(apiResponse).build();
//    }
}
