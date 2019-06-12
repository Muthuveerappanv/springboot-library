package com.springboot.web.common.library.logger;

import java.util.LinkedHashMap;
import java.util.Map;

import com.springboot.web.common.library.util.CustomJacksonMapper;


public class ApplicationLogMessage extends BaseLogMessage {
	// Attributes applicable for logs from class references
	private String className;

	// Actual log message
	private String logMessage;

	private Object payload;

	private String methodName;

	public ApplicationLogMessage(String className, String methodName,
			String logMessage, Object payload) {
		super();
		this.className = className;
		this.methodName = methodName;
		this.logMessage = logMessage;
		this.payload = payload;
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("APPLOG->");
		sb.append(getBaseLog());
		sb.append("|className:" + className);
		sb.append("|logMessage:" + logMessage);
		sb.append("|payload:" + CustomJacksonMapper.convertToJson(payload));
		return sb.toString();
	}
	
	public String asJsonMapStr() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("TYPE", "APPLOG");
		map.putAll(getBaseLogAsMap());
		map.put("className", className);
		map.put("methodName", methodName);
		map.put("logMessage", logMessage);
		map.put("payload", payload);
		String json = CustomJacksonMapper.convertToJson(map);
		return MaskingUtil.maskPaylod(json);
	}

}
