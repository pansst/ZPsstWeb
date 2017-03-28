package com.psst.common.service;

public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 1401593546385403720L;

	public ServiceException() {
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}