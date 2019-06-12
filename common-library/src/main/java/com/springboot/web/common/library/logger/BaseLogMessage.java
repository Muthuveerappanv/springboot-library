package com.springboot.web.common.library.logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.springboot.web.common.library.context.ServiceContext;
import com.springboot.web.common.library.util.ApplicationConstants;

public class BaseLogMessage {

	// Static variable to mention the direction of logging
	public static final String ENTERING = new String("ENTERING");
	public static final String EXITING = new String("EXITING");
	public static final String REST_TEMPLATE_RQST = new String(
			"REST_TEMPLATE_RQST");
	public static final String REST_TEMPLATE_RESP = new String(
			"REST_TEMPLATE_RESP");

	private Date currentDateTime = null;
	private String threadId = null;
	private String hostname = null;

	// Attributes available from ThreadLocal
	private String correlationId = null;
	private String userId = null;
	private String httpMethod = null;
	
	public BaseLogMessage(String httpMethodName) {
		currentDateTime = new Date();
		Thread currentThread = Thread.currentThread();
		threadId = currentThread.getId() + "-" + currentThread.getName() + "("
				+ currentThread.getThreadGroup().getName() + ")";
		correlationId = ServiceContext.getCorrelationId();
		userId = ServiceContext.getUserId();
		httpMethod = httpMethodName;
		try {
			hostname = InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
			// ignore
		}
	}

	public BaseLogMessage() {
		this(null);
	}
	
	public String getBaseLog() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSSZ");

		StringBuffer sb = new StringBuffer();
		sb.append("|datetime:" + sdf.format(currentDateTime));
		sb.append("|hostname:" + hostname);
		sb.append("|threadId:" + threadId);
		sb.append("|userId:" + userId);
		sb.append("|correlationId:" + correlationId);
		sb.append("|httpMethod:" + httpMethod);

		return sb.toString();
	}
	public Map<String, Object> getBaseLogAsMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("hostname", hostname);
		map.put("userId", userId);
		map.put("correlationId", correlationId);
		map.put("httpMethod", httpMethod);
		return map;
	}

	public static String constructLogNm(String appName, String appVersion,
			Boolean isSysLog) {
		String logTypeInd = null;
		String filteredAppNm = StringUtils.replaceAll(appName,
				ApplicationConstants.SPL_CHAR_REGEX, "");
		if (StringUtils.isEmpty(appName)) {
			appName = "default";
		}
		if (StringUtils.isEmpty(appVersion)) {
			appVersion = "v0";
		}
		if (isSysLog) {
			// syslogs - rqst/resp/exceptions
			logTypeInd = "sl";
		} else {
			// applicatoin logs written per operation
			logTypeInd = "al";
		}
		return StringUtils.join(
				Arrays.asList(filteredAppNm, appVersion, logTypeInd),
				ApplicationConstants.UNDERSCORE);
	}
}
