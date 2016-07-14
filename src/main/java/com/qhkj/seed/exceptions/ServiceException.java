package com.qhkj.seed.exceptions;

/**
 * 所有的service错误抛出该异常
 */
public class ServiceException extends RuntimeException {
	
	private static final long serialVersionUID = 914903943916676680L;

	public ServiceException(String msg, Throwable t) {
		super(msg, t);
	}

	public ServiceException(String msg) {
		super(msg);
	}
}
