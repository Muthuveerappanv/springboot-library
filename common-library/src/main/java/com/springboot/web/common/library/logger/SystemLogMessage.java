package com.springboot.web.common.library.logger;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.springboot.web.common.library.util.CustomJacksonMapper;

public class SystemLogMessage extends BaseLogMessage {
	// Attributes applicable for logs from point-cuts
	private String className = null;
	private String methodName = null;

	// Attributes applicable for logs from http filters
	private String requestUrl = null;
	private Map<String, String> httpHeaders = null;
	private String httpResponseCode = null;
	private String payload = null;

	// Caller provided details
	private String logMessage = null;
	private String direction = null;
	private Long reqStartTime = null;
	private String httpMethod = null;

	public SystemLogMessage(String logMessage) {
		super();
		this.logMessage = logMessage;
	}

	public SystemLogMessage(String className, String methodName,
			String logMessage) {
		super();
		this.className = className;
		this.methodName = methodName;
		this.logMessage = logMessage;
	}

	public SystemLogMessage(String direction, String className,
			String methodName, String payload) {
		super();
		this.direction = direction;
		this.className = className;
		this.methodName = methodName;
		this.payload = payload;
	}

	public SystemLogMessage(String direction, String requestUrl) {
		super();
		this.direction = direction;
		this.requestUrl = requestUrl;
	}

	/*
	 * public SystemLogMessage(String direction, String requestUrl, Map<String,
	 * String> httpHeaders, String httpMethod) { super(httpMethod);
	 * this.direction = direction; this.requestUrl = requestUrl;
	 * this.httpHeaders = httpHeaders; }
	 */

	public SystemLogMessage(String direction, String requestUrl,
			Map<String, String> httpHeaders, String payload,
			String httpMethod) {
		super(httpMethod);
		this.direction = direction;
		this.requestUrl = requestUrl;
		this.httpHeaders = httpHeaders;
		this.payload = payload;
		this.httpMethod = httpMethod;
	}

	public SystemLogMessage(String direction, String requestUrl,
			String httpResponseCode, Map<String, String> httpHeaders,
			String httpMethod) {
		super(httpMethod);
		this.direction = direction;
		this.requestUrl = requestUrl;
		this.httpResponseCode = httpResponseCode;
		this.httpHeaders = httpHeaders;
		this.httpMethod = httpMethod;
	}

	public SystemLogMessage(String direction, String requestUrl,
			String httpResponseCode, Map<String, String> httpHeaders,
			String payload, String httpMethod) {
		super(httpMethod);
		this.direction = direction;
		this.requestUrl = requestUrl;
		this.httpResponseCode = httpResponseCode;
		this.httpHeaders = httpHeaders;
		this.payload = payload;
		this.httpMethod = httpMethod;
	}

	public SystemLogMessage(String direction, String requestUrl,
			String httpResponseCode, Map<String, String> httpHeaders,
			String payload, Long reqStartTime, String httpMethod) {
		super(httpMethod);
		this.direction = direction;
		this.requestUrl = requestUrl;
		this.httpResponseCode = httpResponseCode;
		this.httpHeaders = httpHeaders;
		this.payload = payload;
		this.reqStartTime = reqStartTime;
		this.httpMethod = httpMethod;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("SYSLOG->");
		sb.append(getBaseLog());
		sb.append("|direction:" + direction);
		sb.append("|requestUrl:" + requestUrl);
		sb.append("|httpResponse:" + httpResponseCode);
		sb.append("|httpHeaders:" + httpHeaders);
		sb.append("|className:" + className);
		sb.append("|methodName:" + methodName);
		sb.append("|logMessage:" + logMessage);
		sb.append("|payload:" + payload);
		if (reqStartTime != null) {
			sb.append("|timeTaken:"
					+ (System.currentTimeMillis() - reqStartTime) + "ms");
		}
		return sb.toString();
	}

	public String asJsonMapStr() {
		String jsonPayload = CustomJacksonMapper.convertToJson(returnLogAsMap());
		return MaskingUtil.maskPaylod(jsonPayload);
	}

	public String errJsonMapStr(Exception e) {
		Map<String, Object> logAsMap = returnLogAsMap();
		if (e != null) {
			String stacktrace = ExceptionUtils.getRootCauseStackTrace(e)
					.toString();
			if (stacktrace != null && stacktrace.length() > 1000) {
				stacktrace = ExceptionUtils.getRootCauseStackTrace(e).toString()
						.substring(1000);
			}
			String errorMsg = StringUtils.join(
					ExceptionUtils.getMessage(e).toString(), stacktrace, "|");
			logAsMap.put("ExceptionStackTrace", errorMsg);
		}
		return CustomJacksonMapper.convertToJson(logAsMap);
	}

	public Map<String, Object> returnLogAsMap() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("TYPE", "SYSLOG");
		map.put("DIRECTION", direction);
		map.putAll(getBaseLogAsMap());
		map.put("requestUrl", requestUrl);
		map.put("httpResponse", httpResponseCode);
		if (MapUtils.isNotEmpty(httpHeaders)) {
			map.put("httpHeaders", httpHeaders.toString());
		}
		map.put("className", className);
		map.put("methodName", methodName);
		map.put("logMessage", logMessage);
		map.put("payload", payload);
		if (reqStartTime != null) {
			map.put("timeTaken",
					(System.currentTimeMillis() - reqStartTime) + "ms");
		}
		return map;
	}
}
