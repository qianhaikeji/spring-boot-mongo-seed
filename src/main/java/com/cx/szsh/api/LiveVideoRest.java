package com.cx.szsh.api;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.cx.szsh.models.BaseQueryParams;
import com.cx.szsh.models.Video;
import com.cx.szsh.services.LiveVideoService;
import com.cx.szsh.utils.RestfulHelper;

/**
 * 直播业务视频接口
 */
@Component
@Path("/live/videos")
public class LiveVideoRest extends BaseRest {
    @Autowired
    private LiveVideoService liveVideoService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getList(@BeanParam BaseQueryParams bps) {
    	Page<Video> list = liveVideoService.getVideoList(bps);
        return Response.ok(list).build();
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVideo(@PathParam("id") String id) {
        Video video = liveVideoService.getVideoDetail(id);
        if (video == null){
        	return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult("视频不存在!")).build();
        }
        return Response.ok(video).build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addVideo(Video video) {
        Video data = liveVideoService.addVideo(video);
        return Response.created(null).entity(data).build();
    }
    
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyVideo(@PathParam("id") String id, Video video) {
    	if (liveVideoService.modifyVideo(id, video)){
    		return Response.noContent().build();
    	}else{
    		return Response.status(Response.Status.BAD_REQUEST).build();
    	}
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteVideo(@PathParam("id") String id) {
    	liveVideoService.deleteVideo(id);
        return Response.noContent().build();
    }
}
