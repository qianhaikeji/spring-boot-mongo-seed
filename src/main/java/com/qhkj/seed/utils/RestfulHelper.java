package com.qhkj.seed.utils;

import java.util.HashMap;
import java.util.Map;

public class RestfulHelper {
	 public static Map<String, Object> okResult(Object data) {
		 Map<String, Object> res = new HashMap<String, Object>();
		 res.put("data", data);
		 return res;
	 }
	 
	 public static Map<String, Object> errorResult(Object data) {
		 Map<String, Object> res = new HashMap<String, Object>();
		 res.put("message", data);
		 return res;
	 }
}
