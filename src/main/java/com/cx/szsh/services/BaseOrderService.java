package com.cx.szsh.services;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import com.cx.szsh.utils.ConfigHelper;

public class BaseOrderService extends BaseService {
	@Autowired
	private ConfigHelper configHelper;
	
	public enum ORDER_STATUS {
		CREATED("已预约"), FINISHED("已完成"), CANCELED("已取消");

		// 定义私有变量
		private String status;

		// 构造函数，枚举类型只能为私有
		private ORDER_STATUS(String status) {
			this.status = status;
		}

		@Override
		public String toString() {
			return this.status;
		}
	};
	
	/**
	 * |----time--|--count-|machineId|
	 * |0xffffffff|0xffffff|0xff     |
	 */
	public String generateOrderId(){
		ObjectId oid = new ObjectId();
		int timestamp = oid.getTimestamp();
		int count = oid.getCounter();
		short machineid = configHelper.getMachineId();
		short cm = (short)((count << 4) | machineid);
		
		byte[] bytes = new byte[6];
		bytes[0] = int3(timestamp);
        bytes[1] = int2(timestamp);
        bytes[2] = int1(timestamp);
        bytes[3] = int0(timestamp);
        bytes[4] = short1(cm);
        bytes[5] = short0(cm);
        
        StringBuilder buf = new StringBuilder(12);
        for (final byte b : bytes) {
            buf.append(String.format("%02x", b & 0xff));
        }

        return buf.toString();
	}
	
	private static byte int3(final int x) {
        return (byte) (x >> 24);
    }

    private static byte int2(final int x) {
        return (byte) (x >> 16);
    }

    private static byte int1(final int x) {
        return (byte) (x >> 8);
    }

    private static byte int0(final int x) {
        return (byte) (x);
    }
    
    private static byte short1(final short x) {
        return (byte) (x >> 8);
    }

    private static byte short0(final short x) {
        return (byte) (x);
    }
}
