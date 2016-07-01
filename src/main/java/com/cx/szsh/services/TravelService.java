package com.cx.szsh.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.cx.szsh.exceptions.ServiceException;
import com.cx.szsh.models.BaseQueryParams;
import com.cx.szsh.models.Food;
import com.cx.szsh.models.Hotel;
import com.cx.szsh.models.HotelRoom;
import com.cx.szsh.models.Restaurant;
import com.cx.szsh.models.Scenic;
import com.cx.szsh.repository.FoodRepo;
import com.cx.szsh.repository.HotelRepo;
import com.cx.szsh.repository.RestaurantRepo;
import com.cx.szsh.repository.ScenicRepo;
import com.mongodb.BasicDBObject;

@Service
public class TravelService extends BaseOrderService {
    @Autowired private ScenicRepo scenicRepo;
    @Autowired private FoodRepo foodRepo;
    @Autowired private RestaurantRepo restaurantRepo;
    @Autowired private HotelRepo hotelRepo;
    
    @Autowired private QidiService qidiService;
    
    public Page<Scenic> getScenicList(BaseQueryParams bps) {
        Pageable pageable = new PageRequest(bps.getOffset(), bps.getLimit());
        return scenicRepo.findAll(pageable);
    }

    public Scenic getScenicDetail(String id) {
        return scenicRepo.findOne(id);
    }
    
    public Page<Food> getFoodList(BaseQueryParams bps) {
    	Pageable pageable = new PageRequest(bps.getOffset(), bps.getLimit());
        return foodRepo.findAll(pageable);
	}

	public Page<Restaurant> getRestaurantList(BaseQueryParams bps) {
		Pageable pageable = new PageRequest(bps.getOffset(), bps.getLimit());
        return restaurantRepo.findAll(pageable);
	}

	public Page<Hotel> getHotelList(BaseQueryParams bps) {
		Pageable pageable = new PageRequest(bps.getOffset(), bps.getLimit());
		List<Criteria> cs = new ArrayList<Criteria>();
		if (bps.getFuzzy() != null) {
			cs.add(new Criteria("name").regex(bps.getFuzzy()));
		}
		
		String[] excludes = {"images", "rooms"};
        return hotelRepo.find(cs, excludes, pageable);
	}
	
	public Hotel getHotel(String id) {
		return hotelRepo.findOne(id);
	}

	public Scenic addScenic(Scenic scenic) throws ServiceException{
        try {
        	Scenic data = scenicRepo.save(scenic);
            
        	try{
            	String notify = String.format("%s 景区已上线，欢迎您的光临！", data.getName());
            	qidiService.pushNotify(notify, QidiService.APP_NAMES.TRAVEL_SCENIC);
            }catch(ServiceException e){
            	logger.warn(e.getMessage());
            }
            
            return data;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }
    
    public Food addFood(Food food) throws ServiceException {
    	try {
            return foodRepo.save(food);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
	}

	public Restaurant addRestaurant(Restaurant restaurant) throws ServiceException {
		try {
            return restaurantRepo.save(restaurant);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
	}

	public Hotel addHotel(Hotel hotel) throws ServiceException {
		try {
            Hotel data = hotelRepo.save(hotel);
            
            try{
            	String notify = String.format("%s 已上线，欢迎您的光临!", data.getName());
            	qidiService.pushNotify(notify, QidiService.APP_NAMES.TRAVEL_HOTEL);
            }catch(ServiceException e){
            	logger.warn(e.getMessage());
            }
            
            return data;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
	}

	public HotelRoom addHotelRoom(String id, HotelRoom room) throws ServiceException {
		Hotel hotel = hotelRepo.findOne(id);
		if (hotel == null) {
			throw new ServiceException("酒店 [" + id + "] 不存在!");
		}
		
		try{
			hotel.addRoom(room);
			hotelRepo.save(hotel);
			return room;
		} catch (Exception e) {
			logger.warn(e.getMessage());
			throw new ServiceException(e.getMessage());
		}
	}

	public void deleteScenic(String id) throws ServiceException {
        try {
            scenicRepo.delete(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void deleteFood(String id) throws ServiceException {
    	try {
            foodRepo.delete(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
	}

	public void deleteRestaurant(String id) throws ServiceException {
		try {
            restaurantRepo.delete(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
	}

	public void deleteHotel(String id) throws ServiceException {
		try {
            hotelRepo.delete(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
	}

	public void deleteHotelRoom(String id, String rid) throws ServiceException {
		List<Criteria> cs = new ArrayList<Criteria>();
		cs.add(new Criteria("id").is(id));
		Update update = new Update().pull("rooms", new BasicDBObject("id", rid));
		try {
			hotelRepo.upsert(cs, update);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }		
	}

	public void modifyScenic(String id, Scenic newScenic) throws ServiceException {
        Scenic exist = scenicRepo.findOne(id);
        if (exist == null) {
            throw new ServiceException("景点 [" + id + "] 不存在!");
        }
        
        try {
            newScenic.setId(id);
            scenicRepo.save(newScenic);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

	public void modifyFood(String id, Food food) throws ServiceException {
		Food exist = foodRepo.findOne(id);
        if (exist == null) {
            throw new ServiceException("美食 [" + id + "] 不存在!");
        }
        
        try {
        	food.setId(id);
            foodRepo.save(food);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
	}

	public void modifyRestaurant(String id, Restaurant restaurant) throws ServiceException {
		Restaurant exist = restaurantRepo.findOne(id);
        if (exist == null) {
            throw new ServiceException("餐厅 [" + id + "] 不存在!");
        }
        
        try {
        	restaurant.setId(id);
        	restaurantRepo.save(restaurant);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
	}

	public void modifyHotel(String id, Hotel hotel) {
		Hotel exist = hotelRepo.findOne(id);
        if (exist == null) {
            throw new ServiceException("酒店 [" + id + "] 不存在!");
        }
        
        try {
        	hotel.setId(id);
        	hotel.setRooms(exist.getRooms());
        	hotelRepo.save(hotel);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
	}

	public void modifyHotelRoom(String id, String rid, HotelRoom room) {
		Hotel hotel = hotelRepo.findOne(id);
        if (hotel == null) {
            throw new ServiceException("酒店 [" + id + "] 不存在!");
        }
        
        int index = 0;
        for (HotelRoom hr : hotel.getRooms()){
        	if (hr.getId().equals(rid)){
        		room.setId(rid);
        		hotel.getRooms().set(index, room);
        		break;
        	}
        	index++;
        }
        
        try {
        	hotelRepo.save(hotel);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
	}

}
