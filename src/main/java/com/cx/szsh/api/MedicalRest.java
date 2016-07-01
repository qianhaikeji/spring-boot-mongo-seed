package com.cx.szsh.api;

import java.util.Collections;
import java.util.Date;
import java.util.List;

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
import com.cx.szsh.models.Hospital;
import com.cx.szsh.models.HospitalDepartment;
import com.cx.szsh.models.HospitalOrder;
import com.cx.szsh.models.MedicalStation;
import com.cx.szsh.services.MedicalService;
import com.cx.szsh.utils.DateHelper;
import com.cx.szsh.utils.PATCH;
import com.cx.szsh.utils.RestfulHelper;

/**
 * 医疗业务视频接口
 */
@Component
@Path("/medical")
public class MedicalRest extends BaseRest {
	@Autowired
	private MedicalService medicalService;
	
    @GET
    @Path("/hospitals")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHospitalList(@BeanParam BaseQueryParams bps) {
        Page<Hospital> list = medicalService.getHospitalList(bps);
        return Response.ok(list).build();
    }

    @GET
	@Path("/hospitals/{hid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHospital(@PathParam("hid") String id) {
	    Hospital hospital = medicalService.getHospitalDetail(id);
	    if (hospital == null){
	        return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult("医院不存在!")).build();
	    }
	    return Response.ok(hospital).build();
	}

	@GET
	@Path("/hospitals/{hid}/departments")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHospitalDepartmentList(@PathParam("hid") String id) {
	    List<HospitalDepartment> list = medicalService.getDepartmentListByHospital(id);
	    return Response.ok(RestfulHelper.okResult(list!=null?list:Collections.emptyList())).build();
	}
	
	@GET
	@Path("/hospitals/{hid}/departments/{did}/orderedNum")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOrderedNum(@PathParam("hid") String hid, @PathParam("did") String did,
			@QueryParam("type") String type, @QueryParam("orderDate") String orderDate) {
		try {
            Date date = DateHelper.toDate(orderDate);
            int count = medicalService.getDepartmentOrderedNum(hid, did, type, date);
            return Response.ok(RestfulHelper.okResult(count)).build();
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
                    .build();
        }
	}

    @GET
    @Path("/stations")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStationList(@BeanParam BaseQueryParams bps) {
        Page<MedicalStation> list = medicalService.getStationList(bps);
        return Response.ok(list).build();
    }

	@GET
	@Path("/orders")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOrderList(@DefaultValue("全部") @QueryParam("status") String status,
			@DefaultValue("全部") @QueryParam("account") String account, 
	        @DefaultValue("全部") @QueryParam("hospitalId") String hospitalId, 
	        @DefaultValue("全部") @QueryParam("departmentId") String departmentId, @BeanParam BaseQueryParams bps) {
	    Page<HospitalOrder> list = medicalService.getOrderList(account, status, hospitalId, departmentId, bps);
	    for (HospitalOrder order : list.getContent()){
	    	medicalService.fillOrder(order);
	    }
	    return Response.ok(list).build();
	}

	@POST
    @Path("/hospitals")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addHospital(Hospital hospital) {
        try {
            Hospital data = medicalService.addHospital(hospital);
            return Response.created(null).entity(data).build();
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
                    .build();
        }
    }

    @POST
	@Path("/hospitals/{hid}/departments")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addHospitalDepartment(@PathParam("hid") String id, HospitalDepartment department) {
	    try {
	    	HospitalDepartment data = medicalService.addHospitalDepartment(id, department);
	        return Response.created(null).entity(data).build();
	    } catch (ServiceException e) {
	        logger.warn(e.getMessage());
	        return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
	                .build();
	    }
	}

	@POST
	@Path("/orders")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addOrder(HospitalOrder order) {
	    try {
	    	Date now = new Date();
	        HospitalOrder data = medicalService.addOrder(order);
	        medicalService.fillOrder(data);
	        return Response.created(null).entity(data).build();
	    } catch (ServiceException e) {
	        logger.warn(e.getMessage());
	        return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
	                .build();
	    }
	}

    @POST
    @Path("/stations")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStation(MedicalStation station) {
        try {
            MedicalStation data = medicalService.addStation(station);
            return Response.created(null).entity(data).build();
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
                    .build();
        }
    }

	@DELETE
    @Path("/hospitals/{hid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteHospital(@PathParam("hid") String id) {
        try {
            medicalService.deleteHospital(id);
            return Response.noContent().build();
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/hospitals/{hid}/departments/{did}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteHospitalDepartment(@PathParam("hid") String hid, @PathParam("did") String did) {
        try {
            medicalService.deleteDepartmentByHospital(hid, did);
            return Response.noContent().build();
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/stations/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteStation(@PathParam("id") String id) {
        try {
            medicalService.deleteStation(id);
            return Response.noContent().build();
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
                    .build();
        }
    }

	@PUT
	@Path("/hospitals/{hid}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response modifyHospital(@PathParam("hid") String id, Hospital hospital) {
		try{
			medicalService.modifyHospital(id, hospital);
	    return Response.noContent().build();
	    } catch (ServiceException e) {
	        logger.warn(e.getMessage());
	        return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
	                .build();
	    }
	}
	
	@PUT
	@Path("/hospitals/{hid}/departments/{did}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response modifyHospitalDepartment(@PathParam("hid") String hid, @PathParam("did") String did, HospitalDepartment department) {
		try{
			medicalService.modifyHospitalDepartment(hid, did, department);
	    return Response.noContent().build();
	    } catch (ServiceException e) {
	        logger.warn(e.getMessage());
	        return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
	                .build();
	    }
	}

    @PUT
    @Path("/stations/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyStation(@PathParam("id") String id, MedicalStation station) {
        try{
            medicalService.modifyStation(id, station);
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
    public Response modifyOrder(@PathParam("id") String id, HospitalOrder order) {
        try {
        	medicalService.modifyOrder(id, order);
            return Response.noContent().build();
        } 
        catch (ServiceException e) {
            logger.warn(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
                    .build();
        }
    }
}
