package com.springboot.web.common.library.exception;

import com.springboot.web.common.library.logger.BaseLogMessage;

public class SystemErrorMessage extends BaseLogMessage {

	private String direction = BaseLogMessage.EXITING;

	//Attributes available from Exception raised
	private String exceptionClass = null;
	private String exceptionMessage = null;
	private int httpStatusCode = 0;
	private String exceptionResponse = null;
	
	public SystemErrorMessage(String exceptionClass, String exceptionMessage, int statusCode, String exceptionResponse)
	{
		super();
		this.exceptionClass = exceptionClass;
		this.exceptionMessage = exceptionMessage;
		this.httpStatusCode = statusCode;
		this.exceptionResponse = exceptionResponse;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();

		sb.append("SYSERR->");
		sb.append(getBaseLog());
		sb.append("|direction:"+direction);
		sb.append("|exceptionClass:"+exceptionClass);
		sb.append("|exceptionMessage:"+exceptionMessage);
		sb.append("|httpStatusCode:"+httpStatusCode);
		sb.append("|exceptionResponse:"+exceptionResponse);
		
		return sb.toString();
	}
}
