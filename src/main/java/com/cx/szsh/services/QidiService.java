package com.cx.szsh.services;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cx.szsh.exceptions.ServiceException;
import com.cx.szsh.models.QidiApp;
import com.cx.szsh.repository.QidiAppRepo;
import com.cx.szsh.utils.QidiHelper;

@Service
public class QidiService extends BaseService {
	@Autowired
	private QidiAppRepo qidiAppRepo;
	@Autowired
    private QidiHelper qidiHelper;
	
	public enum APP_NAMES {
		TRAVEL_HOTEL("酒店"), TRAVEL_SCENIC("本地景点"), 
		CONV_ACTIVITY("志愿者"), CONVE_HOTLINE("便民热线"),
		MEDICAL_HOSPITAL("预约挂号"), MEDICAL_STATION("社区医疗站");

		// 定义私有变量
		private String name;

		// 构造函数，枚举类型只能为私有
		private APP_NAMES(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return this.name;
		}
	};
	
	public String pushNotify(String summary, APP_NAMES appName) throws ServiceException {
		QidiApp app = qidiAppRepo.findByName(appName.toString());
		if (app == null){
			throw new ServiceException("app [" + appName + "] 不存在!");
		}
		
		return qidiHelper.pushNotifyMessage(appName.toString(), summary, app.getKey(), app.getSecret());
	}
}
