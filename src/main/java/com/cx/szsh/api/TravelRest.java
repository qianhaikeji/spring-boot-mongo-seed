package com.cx.szsh.api;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.cx.szsh.exceptions.ServiceException;
import com.cx.szsh.models.BaseQueryParams;
import com.cx.szsh.models.Food;
import com.cx.szsh.models.Hotel;
import com.cx.szsh.models.HotelRoom;
import com.cx.szsh.models.Restaurant;
import com.cx.szsh.models.Scenic;
import com.cx.szsh.services.TravelService;
import com.cx.szsh.utils.RestfulHelper;

/**
 * 旅游业务接口
 */
@Component
@Path("/travel")
public class TravelRest extends BaseRest {
    @Autowired
    private TravelService travelService;

    @GET
    @Path("/scenics")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getScenicList(@BeanParam BaseQueryParams bps) {
        Page<Scenic> list = travelService.getScenicList(bps);
        return Response.ok(list).build();
    }
    
    @GET
    @Path("/scenics/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getScenic(@PathParam("id") String id) {
        Scenic scenic = travelService.getScenicDetail(id);
        return Response.ok(scenic).build();
    }

    @GET
    @Path("/food")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFoodList(@BeanParam BaseQueryParams bps) {
        Page<Food> list = travelService.getFoodList(bps);
        return Response.ok(list).build();
    }

    @GET
    @Path("/restaurants")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRestaurantList(@BeanParam BaseQueryParams bps) {
        Page<Restaurant> list = travelService.getRestaurantList(bps);
        return Response.ok(list).build();
    }

    @GET
    @Path("/hotels")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHotelList(@BeanParam BaseQueryParams bps) {
        Page<Hotel> list = travelService.getHotelList(bps);
        return Response.ok(list).build();
    }

    @GET
    @Path("/hotels/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHotel(@PathParam("id") String id) {
        Hotel data = travelService.getHotel(id);
        return Response.ok(RestfulHelper.okResult(data)).build();
    }
    
    @POST
    @Path("/scenics")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addScenic(Scenic scenic) {
        try {
            Scenic data = travelService.addScenic(scenic);
            return Response.created(null).entity(data).build();
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/food")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addFood(Food food) {
        try {
            Food data = travelService.addFood(food);
            return Response.created(null).entity(data).build();
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/restaurants")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRestaurant(Restaurant restaurant) {
        try {
            Restaurant data = travelService.addRestaurant(restaurant);
            return Response.created(null).entity(data).build();
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/hotels")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addHotel(Hotel hotel) {
        try {
            Hotel data = travelService.addHotel(hotel);
            return Response.created(null).entity(data).build();
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/hotels/{id}/rooms")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addHotelRoom(@PathParam("id") String id, HotelRoom room) {
        try {
            HotelRoom data = travelService.addHotelRoom(id, room);
            return Response.created(null).entity(data).build();
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
                    .build();
        }
    }
    
    @PUT
    @Path("/scenics/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyScenic(@PathParam("id") String id, Scenic scenic) {
        try {
            travelService.modifyScenic(id, scenic);
            return Response.noContent().build();
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/food/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyFood(@PathParam("id") String id, Food food) {
        try {
            travelService.modifyFood(id, food);
            return Response.noContent().build();
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/restaurants/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyRestaurant(@PathParam("id") String id, Restaurant restaurant) {
        try {
            travelService.modifyRestaurant(id, restaurant);
            return Response.noContent().build();
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
                    .build();
        }
    }
    
    @PUT
    @Path("/hotels/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyHotel(@PathParam("id") String id, Hotel hotel) {
        try {
            travelService.modifyHotel(id, hotel);
            return Response.noContent().build();
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
                    .build();
        }
    }
    
    @PUT
    @Path("/hotels/{id}/rooms/{rid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyHotel(@PathParam("id") String id, @PathParam("rid") String rid, HotelRoom room) {
        try {
            travelService.modifyHotelRoom(id, rid, room);
            return Response.noContent().build();
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/scenics/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteScenic(@PathParam("id") String id) {
        try {
            travelService.deleteScenic(id);
            return Response.noContent().build();
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/food/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFood(@PathParam("id") String id) {
        try {
            travelService.deleteFood(id);
            return Response.noContent().build();
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/restaurants/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRestaurant(@PathParam("id") String id) {
        try {
            travelService.deleteRestaurant(id);
            return Response.noContent().build();
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/hotels/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteHotel(@PathParam("id") String id) {
        try {
            travelService.deleteHotel(id);
            return Response.noContent().build();
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/hotels/{id}/rooms/{rid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteHotelRoom(@PathParam("id") String id, @PathParam("rid") String rid) {
        try {
            travelService.deleteHotelRoom(id, rid);
            return Response.noContent().build();
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(RestfulHelper.errorResult(e.getMessage()))
                    .build();
        }
    }
}
