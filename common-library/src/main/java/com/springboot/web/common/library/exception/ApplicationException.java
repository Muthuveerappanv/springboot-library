package com.springboot.web.common.library.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String exceptionMessage;
	private Object payloadObj;
	private Exception rootException;

	public ApplicationException(String message) {
		super(message);
		this.exceptionMessage = message;
	}

	public ApplicationException(String message, Exception rootException) {
		super(message, rootException);
		this.exceptionMessage = message;
		this.rootException = rootException;
	}

	public ApplicationException(String message, Object payloadObj) {
		super(message);
		this.exceptionMessage = message;
		this.payloadObj = payloadObj;
	}

	@Override
	public String getMessage() {
		return exceptionMessage;
	}
}
