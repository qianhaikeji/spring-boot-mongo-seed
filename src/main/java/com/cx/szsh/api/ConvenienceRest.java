package com.cx.szsh.api;

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
import com.cx.szsh.models.Activity;
import com.cx.szsh.models.Actor;
import com.cx.szsh.models.BaseQueryParams;
import com.cx.szsh.models.FareCard;
import com.cx.szsh.models.Hotline;
import com.cx.szsh.models.ImmigrationOrder;
import com.cx.szsh.models.MarryOrder;
import com.cx.szsh.models.MockInvoice;
import com.cx.szsh.models.MockUtilityFee;
import com.cx.szsh.models.RepairOrder;
import com.cx.szsh.models.RepairWorker;
import com.cx.szsh.services.ConvenienceService;
import com.cx.szsh.utils.PATCH;
import com.cx.szsh.utils.RestfulHelper;

/**
 * 便民业务接口
 */
@Component
@Path("/convenience")
public class ConvenienceRest extends BaseRest {
	@Autowired
	private ConvenienceService convenienceService;

	@GET
	@Path("/repair/orders")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRepairOrders(@DefaultValue("全部") @QueryParam("account") String account,
			@DefaultValue("全部") @QueryParam("status") String status, 
			@DefaultValue("全部") @QueryParam("type") String type,
			@BeanParam BaseQueryParams bps) {
		Page<RepairOrder> list = convenienceService.getRepairOrderList(account, status, type, bps);
		return Response.ok(list).build();
	}

	@GET
	@Path("/repair/workers")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWorkers(@QueryParam("type") String type) {
		List<RepairWorker> list = convenienceService.getWorkerList(type);
		return Response.ok(list).build();
	}

	@GET
	@Path("/marry/orders")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMarryOrders(@DefaultValue("全部") @QueryParam("account") String account,
			@DefaultValue("全部") @QueryParam("status") String status, 
			@BeanParam BaseQueryParams bps) {
		Page<MarryOrder> list = convenienceService.getMarryOrderList(account, status, bps);
		return Response.ok(list).build();
	}

	@GET
	@Path("/marry/orders/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMarryOrder(@PathParam("id") String id) {
		MarryOrder data = convenienceService.getMarryOrder(id);
		return Response.ok(RestfulHelper.okResult(data)).build();
	}

	@GET
	@Path("/invoices")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInvoices(@BeanParam BaseQueryParams bps) {
		Page<MockInvoice> list = convenienceService.getInvoiceList(bps);
		return Response.ok(list).build();
	}

	@GET
	@Path("/fees")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFees(@DefaultValue("全部") @QueryParam("type") String type, @BeanParam BaseQueryParams bps) {
		Page<MockUtilityFee> list = convenienceService.getMockFeeList(type, bps);
		return Response.ok(list).build();
	}

	@GET
	@Path("/fees/{account}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFee(@PathParam("account") String account, @QueryParam("type") String type) {
		MockUtilityFee data = convenienceService.getMockFee(account, type);
		return Response.ok(RestfulHelper.okResult(data)).build();
	}

	@GET
	@Path("/hotlines")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHotlines(@DefaultValue("全部") @QueryParam("type") String type, 
			@BeanParam BaseQueryParams bps) {
		Page<Hotline> list = convenienceService.getHotlineList(type, bps);
		return Response.ok(list).build();
	}

	@GET
	@Path("/hotlines/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHotline(@PathParam("id") String id) {
		Hotline data = convenienceService.getHotline(id);
		return Response.ok(RestfulHelper.okResult(data)).build();
	}
	
	@GET
	@Path("/hotlines/types")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHotlineDepartmentType() {
		return Response.ok(RestfulHelper.okResult(convenienceService.getHotlineDepartmentTypeList())).build();
	}

	@GET
	@Path("/invoices/detail")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInvoice(@QueryParam("type") String type, @QueryParam("code") String code,
			@QueryParam("number") String number, @QueryParam("money") String money) {
		MockInvoice data = convenienceService.getMockInvoice(type, code, number, money);
		return Response.ok(RestfulHelper.okResult(data)).build();
	}

	@GET
	@Path("/activities")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActivities(@BeanParam BaseQueryParams bps) {
		Page<Activity> list = convenienceService.getActivityList(bps);
		return Response.ok(list).build();
	}

	@GET
	@Path("/activities/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActivity(@PathParam("id") String id) {
		Activity data = convenienceService.getActivity(id);
		return Response.ok(RestfulHelper.okResult(data)).build();
	}

	@GET
	@Path("/activities/{id}/actors")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActivityActors(@PathParam("id") String id) {
		List<Actor> list = convenienceService.getActivityActorList(id);
		return Response.ok(list).build();
	}
	
	@GET
	@Path("/actors/{id}/activities")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActorActivities(@PathParam("id") String id, @BeanParam BaseQueryParams bps) {
		Page<Actor> list = convenienceService.getActivityListByActor(id, bps);
		return Response.ok(list).build();
	}
	
	@GET
	@Path("/insurance/medicare")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMedicareInsurance(@QueryParam("cardNum") String cardNum, @QueryParam("birthday") String birthday) {
		Object res = convenienceService.getMedicareInsurance(cardNum, birthday);
		return Response.ok(RestfulHelper.okResult(res)).build();
	}
	
	@GET
	@Path("/insurance/endowment")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEndowmentInsurance(@QueryParam("personalNum") String personalNum, @QueryParam("identityCard") String identityCard) {
		Object res = convenienceService.getEndowmentInsurance(personalNum, identityCard);
		return Response.ok(RestfulHelper.okResult(res)).build();
	}
	
	@GET
	@Path("/insurance/unemployment")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUnemploymentInsurance(@QueryParam("personalNum") String personalNum, @QueryParam("identityCard") String identityCard) {
		Object res = convenienceService.getUnemploymentInsurance(personalNum, identityCard);
		return Response.ok(RestfulHelper.okResult(res)).build();
	}

    @GET
    @Path("/farecards")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFareCards(@DefaultValue("全部") @QueryParam("type") String type, @BeanParam BaseQueryParams bps) {
        Page<FareCard> list = convenienceService.getFareCardList(type, bps);
        return Response.ok(list).build();
    }

    @GET
    @Path("/farecards/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFareCard(@PathParam("id") String id) {
        FareCard data = convenienceService.getFareCard(id);
        return Response.ok(RestfulHelper.okResult(data)).build();
    }

    @GET
    @Path("/farecards/phone/{phone}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFareCardByPhone(@PathParam("phone") String phone) {
        FareCard fareCard = convenienceService.getFareCardByPhone(phone);
        if (fareCard == null) {
            String errMessage = "手机号码 [" + phone + "] 匹配不到阳光优待卡，请到民政局办理手续!";
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(errMessage))
            .build();
        } else {
            return Response.ok(RestfulHelper.okResult(fareCard)).build();
        }
    }
    
    @GET
    @Path("/immigration/orders")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getImmigrationOrders(@DefaultValue("全部") @QueryParam("account") String account,
            @DefaultValue("全部") @QueryParam("status") String status, 
            @BeanParam BaseQueryParams bps) {
        Page<ImmigrationOrder> list = convenienceService.getImmigrationOrderList(account, status, bps);
        return Response.ok(list).build();
    }
    
    @POST
	@Path("/fees/mock")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addFee(MockUtilityFee fee) {
		try {
			MockUtilityFee data = convenienceService.addMockFee(fee);
			return Response.created(null).entity(data).build();
		} catch (ServiceException e) {
			logger.warn(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
					.build();
		}
	}

	@POST
	@Path("/repair/orders")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addRepairOrder(RepairOrder order) {
		try {
			RepairOrder data = convenienceService.addRepairOrder(order);
			return Response.created(null).entity(data).build();
		} catch (ServiceException e) {
			logger.warn(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
					.build();
		}
	}

	@POST
	@Path("/repair/workers")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addWorker(RepairWorker worker) {
		try {
			RepairWorker data = convenienceService.addWorker(worker);
			return Response.created(null).entity(data).build();
		} catch (ServiceException e) {
			logger.warn(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
					.build();
		}
	}

	@POST
	@Path("/marry/orders")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addMarryOrder(MarryOrder order) {
		try {
			MarryOrder data = convenienceService.addMarryOrder(order);
			return Response.created(null).entity(data).build();
		} catch (ServiceException e) {
			logger.warn(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
					.build();
		}
	}

	@POST
	@Path("/invoices/mock")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addMockInvoice(MockInvoice invoice) {
		try {
			MockInvoice data = convenienceService.addMockInvoice(invoice);
			return Response.created(null).entity(data).build();
		} catch (ServiceException e) {
			logger.warn(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
					.build();
		}
	}

	@POST
	@Path("/hotlines")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addHotline(Hotline hotline) {
		try {
			Hotline data = convenienceService.addHotline(hotline);
			return Response.created(null).entity(data).build();
		} catch (ServiceException e) {
			logger.warn(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
					.build();
		}
	}

	@POST
	@Path("/activities")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addActivity(Activity activity) {
		try {
			Activity data = convenienceService.addActivity(activity);
			return Response.created(null).entity(data).build();
		} catch (ServiceException e) {
			logger.warn(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
					.build();
		}
	}

	@POST
	@Path("/activities/{id}/actors")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addActivityActor(@PathParam("id") String id, Actor actor) {
		try {
			Actor data = convenienceService.addActivityActor(id, actor);
			return Response.created(null).entity(data).build();
		} catch (ServiceException e) {
			logger.warn(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
					.build();
		}
	}

    @POST
    @Path("/farecards")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addFareCard(FareCard fareCard) {
        try {
            FareCard data = convenienceService.addFareCard(fareCard);
            return Response.created(null).entity(data).build();
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/immigration/orders")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addImmigrationOrder(ImmigrationOrder order) {
        try {
            ImmigrationOrder data = convenienceService.addImmigrationOrder(order);
            return Response.created(null).entity(data).build();
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
                    .build();
        }
    }

	@DELETE
	@Path("/fees/mock/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteMockFee(@PathParam("id") String id) {
		try {
			convenienceService.deleteMockFee(id);
			return Response.noContent().build();
		} catch (ServiceException e) {
			logger.warn(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
					.build();
		}
	}

	@DELETE
	@Path("/repair/workers/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteWorker(@PathParam("id") String id) {
		try {
			convenienceService.deleteWorker(id);
			return Response.noContent().build();
		} catch (ServiceException e) {
			logger.warn(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
					.build();
		}
	}

	@DELETE
	@Path("/invoices/mock/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteMockInvoice(@PathParam("id") String id) {
		try {
			convenienceService.deleteMockInvoice(id);
			return Response.noContent().build();
		} catch (ServiceException e) {
			logger.warn(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
					.build();
		}
	}

	@DELETE
	@Path("/hotlines/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteHotline(@PathParam("id") String id) {
		try {
			convenienceService.deleteHotline(id);
			return Response.noContent().build();
		} catch (ServiceException e) {
			logger.warn(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
					.build();
		}
	}

	@DELETE
	@Path("/activities/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteActivity(@PathParam("id") String id) {
		try {
			convenienceService.deleteActivity(id);
			return Response.noContent().build();
		} catch (ServiceException e) {
			logger.warn(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
					.build();
		}
	}

	@PATCH
	@Path("/repair/orders/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response modifyRepairOrder(@PathParam("id") String id, RepairOrder order) {
		try {
			convenienceService.modifyRepairOrder(id, order);
			return Response.noContent().build();
		} catch (ServiceException e) {
			logger.warn(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
					.build();
		}
	}

	@PATCH
	@Path("/repair/workers/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response modifyWorker(@PathParam("id") String id, RepairWorker worker) {
		try {
			convenienceService.modifyWorker(id, worker);
			return Response.noContent().build();
		} catch (ServiceException e) {
			logger.warn(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
					.build();
		}
	}

	@PATCH
	@Path("/marry/orders/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response modifyMarryOrder(@PathParam("id") String id, MarryOrder order) {
		try {
			convenienceService.modifyMarryOrder(id, order);
			return Response.noContent().build();
		} catch (ServiceException e) {
			logger.warn(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
					.build();
		}
	}

	@PATCH
	@Path("/hotlines/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response modifyHotline(@PathParam("id") String id, Hotline hotline) {
		try {
			convenienceService.modifyHotline(id, hotline);
			return Response.noContent().build();
		} catch (ServiceException e) {
			logger.warn(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
					.build();
		}
	}

    @PATCH
    @Path("/farecards/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyFareCard(@PathParam("id") String id, FareCard farecard) {
        try {
            convenienceService.modifyFareCard(id, farecard);
            return Response.noContent().build();
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
                    .build();
        }
    }

    @PATCH
    @Path("/farecards/phone/{phone}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyFareCardByPhone(@PathParam("phone") String phone, FareCard farecard) {
        try {
            convenienceService.modifyFareCardByPhone(phone, farecard);
            return Response.noContent().build();
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/activities/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyActivity(@PathParam("id") String id, Activity activity) {
        try {
            convenienceService.modifyActivity(id, activity);
            return Response.noContent().build();
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
                    .build();
        }
    }

    @PATCH
    @Path("/immigration/orders/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyImmigrationOrder(@PathParam("id") String id, ImmigrationOrder order) {
        try {
            convenienceService.modifyImmigrationOrder(id, order);
            return Response.noContent().build();
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
                    .build();
        }
    }
}
