package com.cx.szsh.api;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.cx.szsh.exceptions.ServiceException;
import com.cx.szsh.models.BaseQueryParams;
import com.cx.szsh.models.MockTrafficPoint;
import com.cx.szsh.models.TrafficOrder;
import com.cx.szsh.services.TrafficService;
import com.cx.szsh.utils.PATCH;
import com.cx.szsh.utils.RestfulHelper;

/**
 * 交通业务接口
 */
@Component
@Path("/traffic")
public class TrafficRest extends BaseRest {
	@Autowired
	private TrafficService trafficService;

	@GET
	@Path("/orders")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOrderList(@DefaultValue("全部") @QueryParam("status") String stauts,
			@DefaultValue("全部") @QueryParam("type") String type, 
			@DefaultValue("全部") @QueryParam("account") String account, @BeanParam BaseQueryParams bps) {
		Page<TrafficOrder> list = trafficService.getOrderList(stauts, type, account, bps);
		return Response.ok(list).build();
	}

	@GET
	@Path("/points")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPointList(@BeanParam BaseQueryParams bps) {
		Page<MockTrafficPoint> list = trafficService.getPointList(bps);
		return Response.ok(list).build();
	}

	@GET
	@Path("/points/detail")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPoint(@QueryParam("name") String name, @QueryParam("idCard") String idCard, 
		@QueryParam("license") String license, @QueryParam("fn") String fn) {
		MockTrafficPoint data = trafficService.getMockPoint(name, idCard, license, fn);
		return Response.ok(RestfulHelper.okResult(data)).build();
	}

	@GET
	@Path("/illegal")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getIllegal(@QueryParam("hphm") String hphm, @QueryParam("hpzl") String hpzl,
			@QueryParam("clsbdh") String clsbdh) {
		try {
			Object res = trafficService.getIllegalRecord(hphm, hpzl, clsbdh);
			return Response.ok(RestfulHelper.okResult(res)).build();
		} catch (ServiceException e) {
			logger.warn(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
					.build();
		}
	}

	@GET
	@Path("/orders/types")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOrderTypeList() {
		String[] list = trafficService.getOrderTypeList();
		return Response.ok(list).build();
	}

	@POST
	@Path("/orders")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addOrder(TrafficOrder order) {
		try {
			TrafficOrder data = trafficService.addOrder(order);
			return Response.created(null).entity(data).build();
		} catch (ServiceException e) {
			logger.warn(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
					.build();
		}
	}

	@POST
	@Path("/points")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addPoint(MockTrafficPoint point) {
		try {
			MockTrafficPoint data = trafficService.addPoint(point);
			return Response.created(null).entity(data).build();
		} catch (ServiceException e) {
			logger.warn(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
					.build();
		}
	}

	@DELETE
	@Path("/points/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteMockPoint(@PathParam("id") String id) {
		try {
			trafficService.deleteMockPoint(id);
			return Response.noContent().build();
		} catch (ServiceException e) {
			logger.warn(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
					.build();
		}
	}

	@PUT
    @Path("/points/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyMockPoint(@PathParam("id") String id, MockTrafficPoint point) {
        try {
        	trafficService.modifyMockPoint(id, point);
            return Response.noContent().build();
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
                    .build();
        }
    }

	@PATCH
	@Path("/orders/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response modifyOrder(@PathParam("id") String id, TrafficOrder order) {
		try {
			trafficService.modifyOrderStatus(id, order);
			return Response.noContent().build();
		} catch (ServiceException e) {
			logger.warn(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
					.build();
		}
	}
}
