package com.cx.szsh.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.cx.szsh.exceptions.ServiceException;
import com.cx.szsh.models.BaseQueryParams;
import com.cx.szsh.models.Hospital;
import com.cx.szsh.models.HospitalDepartment;
import com.cx.szsh.models.HospitalOrder;
import com.cx.szsh.models.Hotel;
import com.cx.szsh.models.HotelRoom;
import com.cx.szsh.models.MedicalStation;
import com.cx.szsh.repository.HospitalOrderRepo;
import com.cx.szsh.repository.HospitalRepo;
import com.cx.szsh.repository.MedicalStationRepo;
import com.cx.szsh.utils.DateHelper;
import com.mongodb.BasicDBObject;

@Service
public class MedicalService extends BaseOrderService {
	@Autowired
	private HospitalOrderRepo hospitalOrderRepo;

	@Autowired
	private HospitalRepo hospitalRepo;
	
	@Autowired
	private MedicalStationRepo medicalStationRepo;
	
	@Autowired private QidiService qidiService;

	public Page<Hospital> getHospitalList(BaseQueryParams bps) {
		Pageable pageable = new PageRequest(bps.getOffset(), bps.getLimit());
		List<Criteria> cs = new ArrayList<Criteria>();
		
		String[] excludes = {"departments"};
        return hospitalRepo.find(cs, excludes, pageable);
	}

	public Hospital getHospitalDetail(String id) {
		return hospitalRepo.findOne(id);
	}

	public Hospital getHospitalDepartmentDetail(String id) {
		return hospitalRepo.findByDepartmentId(new ObjectId(id));
	}

	public List<HospitalDepartment> getDepartmentListByHospital(String id) {
		Hospital hospital = getHospitalDetail(id);
		return (hospital != null) ? hospital.getDepartments() : null;
	}

	public Page<HospitalOrder> getOrderList(String account, String status, String hospitalId, String departmentId,
			BaseQueryParams bps) {
		Pageable pageable = new PageRequest(bps.getOffset(), bps.getLimit(), Direction.DESC, "createdTime");
		List<Criteria> cs = new ArrayList<Criteria>();
		if (!"全部".equals(status)) {
			cs.add(new Criteria("status").is(status));
		}
		
		if (!"全部".equals(account)) {
			cs.add(new Criteria("account").is(account));
		}

		if (!"全部".equals(hospitalId)) {
			cs.add(new Criteria("hospitalId").is(hospitalId));
		}

		if (!"全部".equals(departmentId)) {
			cs.add(new Criteria("departmentId").is(departmentId));
		}

		if (bps.getFuzzy() != null) {
			cs.add(new Criteria("phone").regex(bps.getFuzzy()));
		}
		
		return hospitalOrderRepo.find(cs, pageable);
	}
	
	public int getDepartmentOrderedNum(String hospitalId, String departmentId, String type, Date orderDate){
		return hospitalOrderRepo.countByHospitalIdAndDepartmentIdAndTypeAndOrderDate(hospitalId, departmentId, type, orderDate);
	}

	public Page<MedicalStation> getStationList(BaseQueryParams bps) {
		Pageable pageable = new PageRequest(bps.getOffset(), bps.getLimit());
		return medicalStationRepo.findAll(pageable);
	}

	public Hospital addHospital(Hospital hospital) throws ServiceException {
		try {
			Hospital data = hospitalRepo.save(hospital);
        	try{
            	String notify = String.format("%s 线上预约挂号，上线了!", data.getName());
            	qidiService.pushNotify(notify, QidiService.APP_NAMES.MEDICAL_HOSPITAL);
            }catch(ServiceException e){
            	logger.warn(e.getMessage());
            }
			return data;
		} catch (DuplicateKeyException e) {
			throw new ServiceException("医院 [" + hospital.getName() + "] 已存在，不可重复添加!");
		}
	}

	public HospitalDepartment addHospitalDepartment(String id, HospitalDepartment department) throws ServiceException {
		Hospital hospital = hospitalRepo.findOne(id);
		if (hospital == null) {
			throw new ServiceException("医院 [" + id + "] 不存在!");
		}

		try {
			hospital.addDepartment(department);
			hospitalRepo.save(hospital);
			return department;
		} catch (Exception e) {
			logger.warn(e.getMessage());
			throw new ServiceException(e.getMessage());
		}
	}

	public HospitalOrder addOrder(HospitalOrder order) throws ServiceException {
		if (!hospitalRepo.exists(order.getHospitalId())) {
			throw new ServiceException("无效的医院!");
		}
		
		try{
			ObjectId odid = new ObjectId(order.getDepartmentId());
			if (hospitalRepo.findByDepartmentId(odid) == null) {
				throw new ServiceException("无效的科室!");
			}
		}catch(IllegalArgumentException e){
			throw new ServiceException("无效的科室!");
		}
		
		if (DateHelper.isAfterDate(new Date(), order.getOrderDate())) {
			throw new ServiceException("无效的预约时间!");
		}
		
		if (DateHelper.isAfterDate(new Date(), order.getOrderDate())) {
			throw new ServiceException("无效的预约时间!");
		}

		if (hasValidOrder(order.getAccount(), order.getDepartmentId())) {
			throw new ServiceException("您已经预约了该业务，请勿重复预约!");
		}

		order.setOrderId(generateOrderId());
		order.setStatus(ORDER_STATUS.CREATED.toString());
		
		try {
			return hospitalOrderRepo.save(order);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
	}

	public MedicalStation addStation(MedicalStation station) throws ServiceException {
		try {
			return medicalStationRepo.save(station);
		} catch (DuplicateKeyException e) {
			throw new ServiceException("医疗站 [" + station.getName() + "] 已存在，不可重复添加!");
		}
	}

	public void modifyHospital(String id, Hospital hospital) throws ServiceException {
		Hospital exist = hospitalRepo.findOne(id);
		if (exist == null) {
			throw new ServiceException("医院 [" + id + "] 不存在!");
		}

		try {
			exist.setName(hospital.getName());
			exist.setLevel(hospital.getLevel());
			exist.setImage(hospital.getImage());
			exist.setAddr(hospital.getAddr());
			exist.setPhone(hospital.getPhone());
			exist.setIntro(hospital.getIntro());
			hospitalRepo.save(exist);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			throw new ServiceException(e.getMessage());
		}
	}

	public void modifyHospitalDepartment(String hid, String did, HospitalDepartment department) {
		Hospital hospital = hospitalRepo.findOne(hid);
		if (hospital == null) {
			throw new ServiceException("医院 [" + hid + "] 不存在!");
		}
        
        int index = 0;
        for (HospitalDepartment hd : hospital.getDepartments()){
        	if (hd.getId().equals(did)){
        		department.setId(did);
        		hospital.getDepartments().set(index, department);
        		break;
        	}
        	index++;
        }
        
        try {
        	hospitalRepo.save(hospital);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
	}

	public void modifyOrder(String id, HospitalOrder order) throws ServiceException {
		HospitalOrder exist = hospitalOrderRepo.findOne(id);
		if (exist == null) {
			throw new ServiceException("订单 [" + id + "] 不存在!");
		}

		try {
			exist.setStatus(order.getStatus());
			hospitalOrderRepo.save(exist);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	public void modifyStation(String id, MedicalStation station) throws ServiceException {
		MedicalStation exist = medicalStationRepo.findOne(id);
		if (exist == null) {
			throw new ServiceException("医疗站 [" + id + "] 不存在!");
		}

		try {
			if (!exist.getOpeningTime().equals(station.getOpeningTime())){
				try{
	            	String notify = String.format("医疗站 %s 服务时间调整为 %s。对您带来的不便我们深感歉意！", station.getName(), station.getOpeningTime());
	            	qidiService.pushNotify(notify, QidiService.APP_NAMES.MEDICAL_STATION);
	            }catch(ServiceException e){
	            	logger.warn(e.getMessage());
	            }
			}
			
			exist.setName(station.getName());
			exist.setAddr(station.getAddr());
			exist.setPhone(station.getPhone());
			exist.setOpeningTime(station.getOpeningTime());
			exist.setService(station.getService());
			exist.setImage(station.getImage());
			medicalStationRepo.save(exist);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	public void deleteHospital(String id) throws ServiceException {
		try {
			hospitalRepo.delete(id);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	public void deleteDepartmentByHospital(String hid, String did) throws ServiceException {
		List<Criteria> cs = new ArrayList<Criteria>();
		cs.add(new Criteria("id").is(hid));
		Update update = new Update().pull("departments", new BasicDBObject("id", did));
		try {
			hospitalRepo.upsert(cs, update);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	public void deleteStation(String id) throws ServiceException {
		try {
			medicalStationRepo.delete(id);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	public void fillOrder(HospitalOrder order) {
		Hospital hospital = getHospitalDetail(order.getHospitalId());
		if (hospital != null) {
			order.setHospitalName(hospital.getName());
			for (HospitalDepartment dep : hospital.getDepartments()) {
				if (dep.getId().equals(order.getDepartmentId())) {
					order.setDepartmentName(dep.getName());
				}
			}
		}
	}

	private boolean hasValidOrder(String account, String did) {
		List<Criteria> cs = new ArrayList<Criteria>();
		cs.add(new Criteria("account").is(account));
		cs.add(new Criteria("departmentId").is(did));
		cs.add(new Criteria("status").is(ORDER_STATUS.CREATED.toString()));
		cs.add(new Criteria("orderDate").gte(DateHelper.toDate(new Date())));
		return hospitalOrderRepo.exists(cs);
	}

}
