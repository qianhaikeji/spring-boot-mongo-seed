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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

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
import com.cx.szsh.repository.ActivityRepo;
import com.cx.szsh.repository.ActorRepo;
import com.cx.szsh.repository.FareCardRepo;
import com.cx.szsh.repository.HotlineRepo;
import com.cx.szsh.repository.ImmigrationOrderRepo;
import com.cx.szsh.repository.MarryOrderRepo;
import com.cx.szsh.repository.MockInvoiceRepo;
import com.cx.szsh.repository.MockUtilityFeeRepo;
import com.cx.szsh.repository.RepairOrderRepo;
import com.cx.szsh.repository.RepairWokerRepo;
import com.cx.szsh.utils.DateHelper;
import com.cx.szsh.utils.SpiderHelper;

@Service
public class ConvenienceService extends BaseOrderService {

    @Autowired private MockUtilityFeeRepo mockUtilityFeeRepo;
    @Autowired private RepairWokerRepo repairWokerRepo;
    @Autowired private RepairOrderRepo repairOrderRepo;
    @Autowired private MockInvoiceRepo mockInvoiceRepo;
    @Autowired private HotlineRepo hotlineRepo;
    @Autowired private FareCardRepo fareCardRepo;
    @Autowired private ActivityRepo activityRepo;
    @Autowired private ActorRepo actorRepo;
    @Autowired private MarryOrderRepo marryOrderRepo;
    @Autowired private ImmigrationOrderRepo immigrationOrderRepo;
    
    @Autowired private QidiService qidiService;
    @Autowired private SpiderHelper spiderHelper;

    public static final String repairOrderTypes[] = { "水", "电", "气" };
    
    public static final String immigrationOrderCredentialTypes[] = { "出国护照", "来往港澳通行证", "来往台湾通行证" };
    
    public static final String immigrationOrderApplyTypes[] = { 
    		"首次普通预约", "持证补发申请", "持证换发申请", "护照加注申请", "护照失效重新申请", "港澳签注申请", "来往台湾签注申请"
		};

    public static final String hotlineDepartmentTypes[] = {"政府服务", "生活便利", "监督投诉", "医疗健康", "其他"};

    public static final String invoiceTypes[] = {"代开发票", "电子发票", "其它发票"};
    
    public enum INSURANCE_TYPE {
        MEDICARE("医疗保险"), ENDOWMENT("养老保险"), UNEMPLOYMENT("事业保险");
        private String type;
        private INSURANCE_TYPE(String type) {
            this.type = type;
        }
        @Override
        public String toString() {
            return this.type;
        }
    };
    
    public static final String fareCardTypes[] = { "优待卡", "阳光卡" };

    public MockUtilityFee getMockFee(String account, String type) {
        return mockUtilityFeeRepo.findByAccountAndType(account, type);
    }

    public Page<MockUtilityFee> getMockFeeList(String type, BaseQueryParams bps) {
        Pageable pageable = new PageRequest(bps.getOffset(), bps.getLimit(), Direction.DESC, "createdTime");
        List<Criteria> cs = new ArrayList<Criteria>();

        if (!"全部".equals(type)) {
            cs.add(new Criteria("type").is(type));
        }

        return mockUtilityFeeRepo.find(cs, pageable);
    }

    public Page<RepairOrder> getRepairOrderList(String account, String status, String type, BaseQueryParams bps) {
        Pageable pageable = new PageRequest(bps.getOffset(), bps.getLimit(), Direction.DESC, "createdTime");
        List<Criteria> cs = new ArrayList<Criteria>();
        
        if (!"全部".equals(type)) {
            cs.add(new Criteria("type").is(type));
        }
        
        if (!"全部".equals(account)) {
            cs.add(new Criteria("account").is(account));
        }

        if (!"全部".equals(status)) {
            cs.add(new Criteria("stauts").is(status));
        }

        if (bps.getFuzzy() != null) {
            cs.add(new Criteria("phone").regex(bps.getFuzzy()));
        }

        return repairOrderRepo.find(cs, pageable);
    }

    public List<RepairWorker> getWorkerList(String type) {
        return repairWokerRepo.findByType(type);
    }

    public Page<MarryOrder> getMarryOrderList(String account, String status, BaseQueryParams bps) {
		Pageable pageable = new PageRequest(bps.getOffset(), bps.getLimit(), Direction.DESC, "createdTime");
        List<Criteria> cs = new ArrayList<Criteria>();
        
        if (!"全部".equals(account)) {
            cs.add(new Criteria("account").is(account));
        }

        if (!"全部".equals(status)) {
            cs.add(new Criteria("stauts").is(status));
        }

        return marryOrderRepo.find(cs, pageable);
	}

	public MarryOrder getMarryOrder(String id) {
        return marryOrderRepo.findOne(id);
	}

	public Page<MockInvoice> getInvoiceList(BaseQueryParams bps) {
        Pageable pageable = new PageRequest(bps.getOffset(), bps.getLimit(), Direction.DESC, "createdTime");
        return mockInvoiceRepo.findAll(pageable);
    }

    public MockInvoice getMockInvoice(String type, String code, String number, String money) {
    	if ("其它发票".equals(type)){
    		return mockInvoiceRepo.findByCodeAndNumber(code, number);
    	}else{
    		return mockInvoiceRepo.findByTypeAndCodeAndNumberAndMoney(type, code, number, money);
    	}
    }
    
    public String[] getHotlineDepartmentTypeList() {
        return hotlineDepartmentTypes;
    }

    public Page<Hotline> getHotlineList(String type, BaseQueryParams bps) {
    	Pageable pageable = new PageRequest(bps.getOffset(), bps.getLimit());
        List<Criteria> cs = new ArrayList<Criteria>();
        
        if (!"全部".equals(type)) {
            cs.add(new Criteria("type").is(type));
        }

        return hotlineRepo.find(cs, pageable);
    }

    public Hotline getHotline(String id) {
        return hotlineRepo.findOne(id);
    }

    public Page<Activity> getActivityList(BaseQueryParams bps) {
    	Pageable pageable = new PageRequest(bps.getOffset(), bps.getLimit(), Direction.DESC, "createdTime");    	
    	Page<Activity> results = activityRepo.findAll(pageable);
    	for(Activity activity : results.getContent()){
    		activity.setCurNum(getActivityActorNum(activity.getId()));
    	}
    	return results;
	}

	public Activity getActivity(String id) {
		Activity activity = activityRepo.findOne(id);
		if (activity != null){
			activity.setCurNum(getActivityActorNum(id));
		}
		return activity;
	}

	public List<Actor> getActivityActorList(String id) {
		return actorRepo.findByActivityId(id);
	}
	
	public int getActivityActorNum(String id) {
		return actorRepo.countByActivityId(id);
	}
	

	public Page<Actor> getActivityListByActor(String id, BaseQueryParams bps) {
		Pageable pageable = new PageRequest(bps.getOffset(), bps.getLimit(), Direction.DESC, "createdTime");
        List<Criteria> cs = new ArrayList<Criteria>();

        cs.add(new Criteria("account").is(id));

        return actorRepo.find(cs, pageable);
	}

	public Page<FareCard> getFareCardList(String type, BaseQueryParams bps) {
	    Pageable pageable = new PageRequest(bps.getOffset(), bps.getLimit(), Direction.DESC, "createdTime");
	    List<Criteria> cs = new ArrayList<Criteria>();
	
	    if (!"全部".equals(type)) {
	        cs.add(new Criteria("type").is(type));
	    }
	
	    if (bps.getFuzzy() != null) {
	        cs.add(new Criteria("name").regex(bps.getFuzzy()));
	    }
	    
	    return fareCardRepo.find(cs, pageable);
	}

	public FareCard getFareCard(String id) {
	    return fareCardRepo.findOne(id);
	}

    public FareCard getFareCardByPhone(String phone) {
        return fareCardRepo.findByPhone(phone);
    }
    
	public Object getMedicareInsurance(String cardNum, String birthday) throws ServiceException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("method", "getClientInfo");
        params.put("data", 
                "{\"transcode\":\"3000\","
                + "\"insurancetype\":\"310\","
                + "\"areano\":\"5323\","
                + "\"inputdata\":{"
                + "\"bac001\":" + cardNum + ","
                + "\"aac006\":" + birthday + "}"
                + "}");
        try {
            Document doc = fetchInsuranceHtmlDoc(params);
            Map<String, Object> results = new HashMap<String, Object>();
            try{
                results.put("name", doc.select("aac003").first().text());
                results.put("sex", doc.select("aac004").first().text());
                results.put("birthday", doc.select("aac006").first().text());
                results.put("org", doc.select("aab004").first().text());
                results.put("income", doc.select("aae240").first().text());
                results.put("expense", doc.select("bae047").first().text());
                results.put("balance", doc.select("bae048").first().text());
                results.put("status", doc.select("aac031").first().text());
                return results;
            }catch(NullPointerException ne){
                return null;
            }
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }   
    }
    
    public Object getEndowmentInsurance(String personalNum, String identityCard) throws ServiceException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("method", "getClientInfo");
        params.put("data", 
                "{\"transcode\":\"1000\","
                + "\"insurancetype\":\"110\","
                + "\"areano\":\"5323\","
                + "\"inputdata\":{"
                + "\"bac001\":" + personalNum + ","
                + "\"aac002\":" + identityCard + "}"
                + "}");
        
        try {
            Document doc = fetchInsuranceHtmlDoc(params);
            Map<String, Object> results = new HashMap<String, Object>();
            try{
                results.put("name", doc.select("aac003").first().text());
                results.put("sex", doc.select("aac004").first().text());
                results.put("birthday", doc.select("aac006").first().text());
                results.put("org", doc.select("aab004").first().text());
                results.put("orgMoney", doc.select("aic040").first().text());
                results.put("ownMoney", doc.select("aic041").first().text());
                results.put("balance", doc.select("bae048").first().text());
                results.put("personalNum", doc.select("bac001").first().text());
                return results;
            }catch(NullPointerException ne){
                return null;
            }
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }
    
    public Object getUnemploymentInsurance(String personalNum, String identityCard) throws ServiceException {
    	Map<String, String> params = new HashMap<String, String>();
        params.put("method", "getClientInfo");
        params.put("data", 
                "{\"transcode\":\"2000\","
                + "\"insurancetype\":\"210\","
                + "\"areano\":\"5323\","
                + "\"inputdata\":{"
                + "\"bac001\":" + personalNum + ","
                + "\"aac002\":" + identityCard + "}"
                + "}");
        
        try {
            Document doc = fetchInsuranceHtmlDoc(params);
            Map<String, Object> results = new HashMap<String, Object>();
            try{
                results.put("name", doc.select("aac003").first().text());
                results.put("sex", doc.select("aac004").first().text());
                results.put("birthday", doc.select("aac006").first().text());
                results.put("org", doc.select("aab004").first().text());
                results.put("income", doc.select("aic040").first().text());
                results.put("expense", doc.select("aic041").first().text());
                results.put("balance", doc.select("bae048").first().text());
                results.put("personalNum", doc.select("bac001").first().text());
                return results;
            }catch(NullPointerException ne){
                return results;
            }
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public Page<ImmigrationOrder> getImmigrationOrderList(String account, String status, BaseQueryParams bps) {
        Pageable pageable = new PageRequest(bps.getOffset(), bps.getLimit(), Direction.DESC, "createdTime");
        List<Criteria> cs = new ArrayList<Criteria>();
        
        if (!"全部".equals(account)) {
            cs.add(new Criteria("account").is(account));
        }

        if (!"全部".equals(status)) {
            cs.add(new Criteria("stauts").is(status));
        }

        if (bps.getFuzzy() != null) {
            cs.add(new Criteria("phone").regex(bps.getFuzzy()));
        }

        return immigrationOrderRepo.find(cs, pageable);
    }
    
    private Document fetchInsuranceHtmlDoc(Map<String, String> params) throws IOException {
        return spiderHelper.postXmlDocument(spiderHelper.getInsuranceBaseParams().get("url"), params);
    }


    public MockUtilityFee addMockFee(MockUtilityFee fee) throws ServiceException {
        if (Arrays.asList(repairOrderTypes).indexOf(fee.getType()) < 0) {
            throw new ServiceException("无效的类型:" + fee.getType());
        }
        
        MockUtilityFee exist = mockUtilityFeeRepo.findByAccountAndType(fee.getAccount(), fee.getType());
        if (exist != null){
        	fee.setId(exist.getId());
        }
        
        try {
            return mockUtilityFeeRepo.save(fee);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public RepairOrder addRepairOrder(RepairOrder order) throws ServiceException {
        if (Arrays.asList(repairOrderTypes).indexOf(order.getType()) < 0) {
            throw new ServiceException("无效的订单类型:" + order.getType());
        }

        if (order.getOrderDate() == null || DateHelper.isAfterDate(new Date(), order.getOrderDate())) {
            throw new ServiceException("无效的预约时间!");
        }

        if (hasValidRepairOrder(order.getAccount(), order.getType())) {
            throw new ServiceException("您已经预约了该业务，请勿重复预约!");
        }

        order.setStatus(ORDER_STATUS.CREATED.toString());
        order.setOrderId(generateOrderId());

        try {
            return repairOrderRepo.save(order);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public RepairWorker addWorker(RepairWorker worker) throws ServiceException {
        if (Arrays.asList(repairOrderTypes).indexOf(worker.getType()) < 0) {
            throw new ServiceException("无效的类型:" + worker.getType());
        }

        try {
            return repairWokerRepo.save(worker);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public MarryOrder addMarryOrder(MarryOrder order) {
		if (order.getOrderDate() == null || DateHelper.isAfterDate(new Date(), order.getOrderDate())) {
            throw new ServiceException("无效的预约时间!");
        }

        if (hasValidMarryOrder(order.getAccount())) {
            throw new ServiceException("您已经预约了该业务，请勿重复预约!");
        }

        order.setStatus(ORDER_STATUS.CREATED.toString());
        order.setOrderId(generateOrderId());

        try {
            return marryOrderRepo.save(order);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
	}

	public MockInvoice addMockInvoice(MockInvoice invoice) throws ServiceException {
        try {
            return mockInvoiceRepo.save(invoice);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public Hotline addHotline(Hotline hotline) throws ServiceException {
        try {
        	Hotline data = hotlineRepo.save(hotline);
			try{
            	String notify = String.format("%s 便民热线已上线!", data.getName());
            	qidiService.pushNotify(notify, QidiService.APP_NAMES.CONVE_HOTLINE);
            }catch(ServiceException e){
            	logger.warn(e.getMessage());
            }
			
            return data;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public FareCard addFareCard(FareCard fareCard) throws ServiceException {
        if (Arrays.asList(fareCardTypes).indexOf(fareCard.getType()) < 0) {
            throw new ServiceException("无效的卡类型:" + fareCard.getType());
        }

        try {
            return fareCardRepo.save(fareCard);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public Activity addActivity(Activity activity)  throws ServiceException {
		try {
			Activity data = activityRepo.save(activity);
			try{
            	String notify = String.format("%s 志愿者活动，开始线上报名，欢迎您的加入！", data.getTitle());
            	qidiService.pushNotify(notify, QidiService.APP_NAMES.CONV_ACTIVITY);
            }catch(ServiceException e){
            	logger.warn(e.getMessage());
            }
            return data;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
	}

	public Actor addActivityActor(String id, Actor actor) throws ServiceException {
		Activity activity = activityRepo.findOne(id);
    	if (activity == null) {
            throw new ServiceException("活动 [" + id + "] 不存在!");
        }
    	
    	if (hasJoinedActivity(actor.getAccount(), id)){
    		throw new ServiceException("您已经报名了该活动，请勿重复报名!");
    	}
    	
    	try {	
    		actor.setActivity(activity);
    		return actorRepo.save(actor);
    	} catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
	}

    public ImmigrationOrder addImmigrationOrder(ImmigrationOrder order) throws ServiceException {
        if (Arrays.asList(immigrationOrderCredentialTypes).indexOf(order.getCredentialType()) < 0) {
            throw new ServiceException("无效的证件类型:" + order.getCredentialType());
        }
        
        if (Arrays.asList(immigrationOrderApplyTypes).indexOf(order.getApplyType()) < 0) {
            throw new ServiceException("无效的申请类型:" + order.getApplyType());
        }
        
        if (order.getOrderDate() == null || DateHelper.isAfterDate(new Date(), order.getOrderDate())) {
            throw new ServiceException("无效的预约时间!");
        }

        if (hasValidImmigrationOrder(order.getAccount(), order.getCredentialType())) {
            throw new ServiceException("您已经预约了该业务，请勿重复预约!");
        }

        order.setStatus(ORDER_STATUS.CREATED.toString());
        order.setOrderId(generateOrderId());

        try {
            return immigrationOrderRepo.save(order);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }
    
	public void deleteMockFee(String id) throws ServiceException {
        try {
            mockUtilityFeeRepo.delete(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void deleteWorker(String id) throws ServiceException {
        try {
            repairWokerRepo.delete(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void deleteMockInvoice(String id) throws ServiceException {
        try {
            mockInvoiceRepo.delete(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void deleteHotline(String id) throws ServiceException {
        try {
            hotlineRepo.delete(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void deleteActivity(String id) throws ServiceException {
    	try {
            activityRepo.delete(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
	}

	public void modifyRepairOrder(String id, RepairOrder order) throws ServiceException {
        RepairOrder exist = repairOrderRepo.findOne(id);
        if (exist == null) {
            throw new ServiceException("订单 [" + id + "] 不存在!");
        }

        if (order.getStatus() != null) {
            exist.setStatus(order.getStatus());
        }

        if (order.getEvaluation() != 0) {
            exist.setEvaluation(order.getEvaluation());
        }

        if (order.getWorker() != null && order.getWorker().getId() != null) {
            RepairWorker worker = repairWokerRepo.findOne(order.getWorker().getId());
            if (worker == null) {
                throw new ServiceException("维修员 [" + order.getWorker().getId() + "] 不存在!");
            }

            exist.setWorker(worker);
        }

        try {
            repairOrderRepo.save(exist);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void modifyWorker(String id, RepairWorker worker) throws ServiceException {
        RepairWorker exist = repairWokerRepo.findOne(id);
        if (exist == null) {
            throw new ServiceException("维修员 [" + id + "] 不存在!");
        }

        try {
            exist.setName(worker.getName());
            exist.setPhone(worker.getPhone());
            exist.setAvatar(worker.getAvatar());
            exist.setOrderCount(worker.getOrderCount());
            repairWokerRepo.save(exist);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void modifyMarryOrder(String id, MarryOrder order) {
		MarryOrder exist = marryOrderRepo.findOne(id);
        if (exist == null) {
            throw new ServiceException("订单 [" + id + "] 不存在!");
        }

        if (order.getStatus() != null) {
            exist.setStatus(order.getStatus());
        }
		
        try {
            marryOrderRepo.save(exist);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
	}

	public void modifyHotline(String id, Hotline hotline) throws ServiceException {
        Hotline exist = hotlineRepo.findOne(id);
        if (exist == null) {
            throw new ServiceException("便民热线 [" + id + "] 不存在!");
        }

        try {
            exist.setName(hotline.getName());
            exist.setAddr(hotline.getAddr());
            exist.setContact(hotline.getContact());
            exist.setFixPhone(hotline.getFixPhone());
            exist.setPhone(hotline.getPhone());
            exist.setType(hotline.getType());
            exist.setImage(hotline.getImage());
            hotlineRepo.save(exist);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void modifyActivity(String id, Activity activity) throws ServiceException {
    	Activity exist = activityRepo.findOne(id);
    	if (exist == null) {
            throw new ServiceException("活动 [" + id + "] 不存在!");
        }
    	
		try {
			exist.setTitle(activity.getTitle());
			exist.setContent(activity.getContent());
			exist.setActiveStartDate(activity.getActiveStartDate());
			exist.setActiveEndDate(activity.getActiveEndDate());
			exist.setApplyStartDate(activity.getApplyStartDate());
			exist.setApplyEndDate(activity.getApplyEndDate());
			exist.setAddr(activity.getAddr());
			exist.setCover(activity.getCover());
			exist.setOrg(activity.getOrg());
			exist.setOrgBrief(activity.getOrgBrief());
			exist.setContact(activity.getContact());
			exist.setPhone(activity.getPhone());
			exist.setMaxNum(activity.getMaxNum());
			activityRepo.save(exist);
		} catch (Exception e) {
		    throw new ServiceException(e.getMessage());
		}
	}

	public void modifyFareCard(String id, FareCard farecard) throws ServiceException {
        FareCard exist = fareCardRepo.findOne(id);
        if (exist == null) {
            throw new ServiceException("阳光优待卡 ID [" + id + "] 不存在!");
        }

        if (farecard.getName() != null) {
            exist.setName(farecard.getName());
        }

        if (farecard.getPhone() != null) {
            exist.setPhone(farecard.getPhone());
        }
        
        if (farecard.getIdentityCard() != null) {
            exist.setIdentityCard(farecard.getIdentityCard());
        }

        if (farecard.getBirthday() != null) {
            exist.setBirthday(farecard.getBirthday());
        }

        if (farecard.getResident() != null) {
            exist.setResident(farecard.getResident());
        }

        if (farecard.getAddr() != null) {
            exist.setAddr(farecard.getAddr());
        }

        if (farecard.getAvatar() != null) {
            exist.setAvatar(farecard.getAvatar());
        }

        if (farecard.getDisabled() != null) {
            exist.setDisabled(farecard.getDisabled());
        }

        if (farecard.getType() != null) {
            exist.setType(farecard.getType());
        }

        try {
            fareCardRepo.save(exist);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void modifyFareCardByPhone(String phone, FareCard farecard)  throws ServiceException{
        // 移动端用户不能更改 phone 和 type
        farecard.setPhone(null);
        farecard.setType(null);

        FareCard exist = fareCardRepo.findByPhone(phone);
        if (exist == null) {
            throw new ServiceException("手机号码 [" + phone + "] 匹配不到阳光优待卡，请到民政局办理手续!");
        }

        this.modifyFareCard(exist.getId(), farecard);
    }
    
    public void modifyImmigrationOrder(String id, ImmigrationOrder order) {
        ImmigrationOrder exist = immigrationOrderRepo.findOne(id);
        if (exist == null) {
            throw new ServiceException("订单 [" + id + "] 不存在!");
        }

        if (order.getStatus() != null) {
            exist.setStatus(order.getStatus());
        }
        
        try {
            immigrationOrderRepo.save(exist);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }
    
	private boolean hasValidRepairOrder(String account, String type) {
	    List<Criteria> cs = new ArrayList<Criteria>();
	    cs.add(new Criteria("account").is(account));
	    cs.add(new Criteria("type").is(type));
	    cs.add(new Criteria("status").is(ORDER_STATUS.CREATED.toString()));
	    cs.add(new Criteria("orderDate").gte(DateHelper.toDate(new Date())));
	    return repairOrderRepo.exists(cs);
	}

    private boolean hasValidMarryOrder(String account) {
        List<Criteria> cs = new ArrayList<Criteria>();
        cs.add(new Criteria("account").is(account));
        cs.add(new Criteria("status").is(ORDER_STATUS.CREATED.toString()));
        cs.add(new Criteria("orderDate").gte(DateHelper.toDate(new Date())));
        return marryOrderRepo.exists(cs);
    }
	
	private boolean hasValidImmigrationOrder(String account, String credentialType) {
        List<Criteria> cs = new ArrayList<Criteria>();
        cs.add(new Criteria("account").is(account));
        cs.add(new Criteria("credentialType").is(credentialType));
        cs.add(new Criteria("status").is(ORDER_STATUS.CREATED.toString()));
        cs.add(new Criteria("orderDate").gte(DateHelper.toDate(new Date())));
        return immigrationOrderRepo.exists(cs);
    }

    private boolean hasJoinedActivity(String account, String activityId){
    	ObjectId a = new ObjectId(activityId);
		List<Criteria> cs = new ArrayList<Criteria>();
	    cs.add(new Criteria("account").is(account));
	    cs.add(new Criteria("activity.$id").is(new ObjectId(activityId)));
	    return actorRepo.exists(cs);
	}
}
