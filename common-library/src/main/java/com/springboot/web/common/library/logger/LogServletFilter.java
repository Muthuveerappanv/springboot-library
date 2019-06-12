package com.springboot.web.common.library.logger;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.springboot.web.common.library.context.ServiceContext;
import com.springboot.web.common.library.util.ApplicationConstants;

public class LogServletFilter implements Filter {
	private boolean logPayload = false;
	private Logger logger = LoggerFactory.getLogger(LogServletFilter.class);

	public LogServletFilter(boolean logPayload) {
		this.logPayload = logPayload;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		/**********
		 * Setting thread-local object for service context
		 ***************/
		ServiceContext.setContext(request);

		Long currentTime = System.currentTimeMillis();
		if (logPayload) {
			request = new HTTPRequestWrapper((HttpServletRequest) request);
			response = new HTTPResponseWrapper((HttpServletResponse) response);
		}
		logRequest(request, logger);
		chain.doFilter(request, response);
		logResponse(request, response, logger, currentTime);
	}

	private void logRequest(ServletRequest request, Logger logger) {
		String url = null;
		HashMap<String, String> requestHeaders = null;
		SystemLogMessage slm = null;
		String logPayloadString = ApplicationConstants.DEFAULT_LOG_PAYLOAD;
		String httpMethod = null;

		if (request instanceof HttpServletRequest) {
			HttpServletRequest hsr = (HttpServletRequest) request;
			httpMethod = hsr.getMethod();
			url = hsr.getRequestURL().toString();
			if (StringUtils.isNotEmpty(hsr.getQueryString())) {
				String queryStr = hsr.getQueryString();
				url = url + "?" + queryStr;
			}
			if (logPayload) {
				requestHeaders = getHttpReqParams(hsr);
			}
		}

		if (request instanceof HTTPRequestWrapper) {
			httpMethod = ((HTTPRequestWrapper) request).getMethod();
			if (StringUtils
					.isNotEmpty(((HTTPRequestWrapper) request).getPayload())) {
				logPayloadString = ((HTTPRequestWrapper) request).getPayload();
			} ;
		}

		slm = new SystemLogMessage(SystemLogMessage.ENTERING, url,
				requestHeaders, logPayloadString, httpMethod);
		// System.out.println("$$$$$$$$$$$" + slm.toString());
		logger.info(slm.toString());
	}

	private HashMap<String, String> getHttpReqParams(HttpServletRequest hsr) {
		HashMap<String, String> hp = new HashMap<String, String>();
		Enumeration<String> headers = hsr.getHeaderNames();

		// Read HTTP Headers
		while (headers.hasMoreElements()) {
			String key = headers.nextElement();
			hp.put(key, hsr.getHeader(key));
		}
		// Read Query Parameters
		Enumeration<String> params = hsr.getParameterNames();
		while (params.hasMoreElements()) {
			String key = params.nextElement();
			hp.put(key, hsr.getParameter(key));
		}
		return hp;
	}

	private void logResponse(ServletRequest request, ServletResponse response,
			Logger logger, Long reqStartTime) {
		String url = null;
		HashMap<String, String> responseHeaders = null;
		String httpResponseCode = null;
		SystemLogMessage slm = null;
		String logPayloadStr = null;
		String httpMethod = null;

		if (request instanceof HttpServletRequest) {
			HttpServletRequest hsr = (HttpServletRequest) request;
			url = hsr.getRequestURL().toString();
			httpMethod = hsr.getMethod();
		}
		if (response instanceof HttpServletResponse) {
			HttpServletResponse hsr = (HttpServletResponse) response;
			if (logPayload) {
				responseHeaders = getHttpResParams(hsr);
			}
			httpResponseCode = "" + hsr.getStatus();
		}
		if (response instanceof HTTPResponseWrapper) {
			HTTPResponseWrapper httpResponseWrapper = (HTTPResponseWrapper) response;
			if (httpResponseWrapper.getPayload() != null) {
				logPayloadStr = httpResponseWrapper.getPayload();
			} ;
		}

		slm = new SystemLogMessage(SystemLogMessage.EXITING, url,
				httpResponseCode, responseHeaders, logPayloadStr, reqStartTime,
				httpMethod);
		logger.info(slm.toString());
	}

	public static HashMap<String, String> getHttpResParams(
			HttpServletResponse hsr) {
		HashMap<String, String> hp = new HashMap<>();
		hsr.getHeaderNames().forEach(item -> hp.put(item, hsr.getHeader(item)));
		return hp;
	}

	public void destroy() {// ignore
	}

}