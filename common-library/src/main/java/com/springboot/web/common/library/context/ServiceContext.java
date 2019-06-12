package com.springboot.web.common.library.context;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.springboot.web.common.library.util.ApplicationConstants;

public class ServiceContext {

	public static InheritableThreadLocal<Context> context = new InheritableThreadLocal<Context>();

	public static void setContext(ServletRequest request) {
		Map<String, String> headerValues = handleConfigHeader(request);
		String corrId = null;
		String serviceUrl = null;
		String userId = null;

		if (headerValues != null) {
			corrId = headerValues.get("transactionId");
			serviceUrl = headerValues.get("serviceUrl");
			if (headerValues.containsKey("userId")) {
				userId = headerValues.get("userId");
			}
		}
		context.set(new Context(userId, corrId, serviceUrl));
	}

	public static void setContext(String correaltionId) {
		context.set(new Context(null, correaltionId, null));
	}
	private static Map<String, String> handleConfigHeader(
			ServletRequest servletRequest) {
		Map<String, String> map = new HashMap<String, String>();
		if (servletRequest instanceof HttpServletRequest) {
			HttpServletRequest hreq = (HttpServletRequest) servletRequest;
			if (hreq != null) {
				String requestConfigString = hreq.getHeader("transactionId");
				map.put("httpMethod", hreq.getMethod());
				ServiceConfig serviceConfig = new ServiceConfig();
				serviceConfig.setTransactionId(requestConfigString);

				hreq.setAttribute(ApplicationConstants.HDR_ATTRB,
						serviceConfig);
				map.put("serviceUrl", hreq.getRequestURL().toString());
				try {
					UUID.fromString(serviceConfig.getTransactionId());
				} catch (NullPointerException | IllegalArgumentException e) {
					serviceConfig
							.setTransactionId(UUID.randomUUID().toString());
				}
				map.put("transactionId", serviceConfig.getTransactionId());
				if (hreq.getHeader("userId") != null) {
					map.put("userId", hreq.getHeader("userId"));
				}
			}
		}
		return map;
	}
	public static void unsetContext() {
		context.remove();
	}
	public static String getCorrelationId() {
		String retValue = null;
		if (context.get() != null) {
			retValue = context.get().getCorrelationId();
		}
		return retValue;
	}
	public static String getUserId() {
		String retValue = null;
		if (context.get() != null) {
			retValue = context.get().getUserId();
		}
		return retValue;
	}
	public static String getServiceUrl() {
		String retValue = null;
		if (context.get() != null) {
			retValue = context.get().getServiceUrl();
		}
		return retValue;
	}

}
