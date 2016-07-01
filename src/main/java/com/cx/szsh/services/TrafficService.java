package com.cx.szsh.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.cx.szsh.exceptions.ServiceException;
import com.cx.szsh.models.BaseQueryParams;
import com.cx.szsh.models.MockTrafficPoint;
import com.cx.szsh.models.TrafficOrder;
import com.cx.szsh.repository.MockTrafficPointRepo;
import com.cx.szsh.repository.TrafficOrderRepo;
import com.cx.szsh.utils.DateHelper;
import com.cx.szsh.utils.SpiderHelper;

@Service
public class TrafficService extends BaseOrderService {
	@Autowired
	private TrafficOrderRepo orderRepo;
	@Autowired
	private MockTrafficPointRepo mockTrafficPointRepo;
	
	@Autowired
	private SpiderHelper spiderHelper;

	public static final String orderTypes[] = { "机动车注册登记", "机动车注销登记", "补换检验合格标志", "申请/补领机动车登记证书", "车辆档案变更", "发动机号变更", "转入转出登记",
			"车辆过户转移登记", "普通变更登记", "交通违法办理", "其他" };

	public Page<TrafficOrder> getOrderList(String status, String type, String account, BaseQueryParams bps) {
		Pageable pageable = new PageRequest(bps.getOffset(), bps.getLimit(), Direction.DESC, "createdTime");
		List<Criteria> cs = new ArrayList<Criteria>();
		if (!"全部".equals(status)) {
			cs.add(new Criteria("status").is(status));
		}
		
		if (!"全部".equals(account)) {
			cs.add(new Criteria("account").is(account));
		}
		
		if (!"全部".equals(type)) {
			cs.add(new Criteria("type").is(type));
		}
		
		if (bps.getFuzzy() != null) {
			cs.add(new Criteria("phone").regex(bps.getFuzzy()));
		}
		
		return orderRepo.find(cs, pageable);
	}

	public String[] getOrderTypeList() {
		return orderTypes;
	}
	
	public Page<MockTrafficPoint> getPointList(BaseQueryParams bps) {
		Pageable pageable = new PageRequest(bps.getOffset(), bps.getLimit());
		return mockTrafficPointRepo.findAll(pageable);
	}

	public MockTrafficPoint getMockPoint(String name, String idCard, String license, String fn) {
		return mockTrafficPointRepo.findByNameAndIdCardAndLicenseAndFn(name, idCard, license, fn);
	}

	//	@Cacheable(value="cxszshTrafficCache", key="#hphm")
	public Object getIllegalRecord(String hphm, String hpzl, String clsbdh) throws ServiceException {		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("OpenId", spiderHelper.getTrafficIllegalBaseParams().get("OpenId"));
		params.put("PublicId", spiderHelper.getTrafficIllegalBaseParams().get("PublicId"));
		params.put("HPHM", hphm);
		params.put("HPZL", hpzl);
		params.put("CLSBDH", clsbdh);
		try {
			Document doc = spiderHelper.getHtmlDocument(spiderHelper.getTrafficIllegalBaseParams().get("url"), params);
	        Elements doms = doc.select("li[data-role=fieldcontain]");
	        List<String> results = new ArrayList<String>();
	        for (Element dom : doms){
	        	results.add(dom.text());
	        }
	        return results;
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}		
	}

	public TrafficOrder addOrder(TrafficOrder order) throws ServiceException {
		if (Arrays.asList(orderTypes).indexOf(order.getType()) < 0) {
			throw new ServiceException("无效的订单类型:" + order.getType());
		}

		if (order.getOrderDate() == null || DateHelper.isAfterDate(new Date(), order.getOrderDate())) {
			throw new ServiceException("无效的预约时间!");
		}

		if (hasValidOrder(order.getAccount(), order.getType())) {
			throw new ServiceException("您已经预约了该业务，请勿重复预约!");
		}

		order.setStatus(ORDER_STATUS.CREATED.toString());
		order.setOrderId(generateOrderId());
		
		try {
			return orderRepo.save(order);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
	}
	
	public MockTrafficPoint addPoint(MockTrafficPoint point) throws ServiceException {
		try {
			return mockTrafficPointRepo.save(point);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
	}

	public void modifyOrderStatus(String id, TrafficOrder order) throws ServiceException {
	    TrafficOrder exist = null;
	    if (ObjectId.isValid(id)) {
	        exist = orderRepo.findOne(id);
        } else {
            exist = orderRepo.findByOrderId(id);
        }
	    
        if (exist == null) {
            throw new ServiceException("订单 [" + id + "] 不存在!");
        }
		
		try{
			exist.setStatus(order.getStatus());
			orderRepo.save(exist);
		}catch(Exception e){
			throw new ServiceException(e.getMessage());
		}
	}
	
	public void modifyMockPoint(String id, MockTrafficPoint point) throws ServiceException {
		MockTrafficPoint exist = mockTrafficPointRepo.findOne(id);
	    
        if (exist == null) {
            throw new ServiceException("积分记录 [" + id + "] 不存在!");
        }
		
		try{
			point.setId(id);
			mockTrafficPointRepo.save(point);
		}catch(Exception e){
			throw new ServiceException(e.getMessage());
		}
	}

	public void deleteMockPoint(String id) throws ServiceException {
		try{
			mockTrafficPointRepo.delete(id);
		}catch(Exception e){
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * 同一个帐户，同一种类型的订单只能提交一次 判断条件，查找该帐户是否有已预约，且未过期的该类型的订单
	 */
	private boolean hasValidOrder(String account, String type) {
		TrafficOrder order = orderRepo.findValidOrder(account, type, ORDER_STATUS.CREATED.toString(),
				DateHelper.toDate(new Date()));
		if (order != null) {
			return true;
		}
		return false;
	}
}
