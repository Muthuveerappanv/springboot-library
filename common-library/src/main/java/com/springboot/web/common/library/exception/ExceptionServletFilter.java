/*package com.springboot.web.common.library.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import com.springboot.web.common.library.beans.LogStoreEnv;
import com.springboot.web.common.library.context.ServiceContext;
import com.springboot.web.common.library.enums.ErrorOriginEnum;
import com.springboot.web.common.library.logger.LoggerComponent;
import com.springboot.web.common.library.logger.LogServletFilter;
import com.springboot.web.common.library.logger.SystemLogMessage;
import com.springboot.web.common.library.util.ApplicationConstants;
import com.springboot.web.common.library.util.JacksonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ExceptionServletFilter implements Filter {

	private ExceptionMapper exceptionMapper;
	private ObjectMapper mapper = new ObjectMapper();
	private Boolean logPayload;
	private LogStoreEnv logStoreEnv;
	private Map<Object, Object> marketMap;
	private Logger logger = LoggerFactory
			.getLogger(ExceptionServletFilter.class);

	public ExceptionServletFilter(ExceptionMapper exceptionMapper,
			Boolean logPayload, LogStoreEnv logStoreEnv,
			Map<Object, Object> marketMap) {
		this.exceptionMapper = exceptionMapper;
		this.logPayload = logPayload;
		this.logStoreEnv = logStoreEnv;
		this.marketMap = marketMap;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		*//**********
		 * Setting thread-local object for service context
		 ***************//*
		ServiceContext.setContext(request, marketMap);

		try {
			chain.doFilter(request, response);
		} catch (Exception e) {
			byte[] errResponse = null;
			Throwable cause = e.getCause();
			if (cause == null) {
				cause = e;
			}
			ErrorResponse errorResponse = exceptionMapper.mapException(cause,
					ServiceContext.getServiceUrl());
			ErrorOriginEnum errorOrigin = null;
			if (cause != null) {
				retrieveErrorDtl(errorResponse, cause, errorOrigin);
			}
			if (null == errorResponse.getErrorOrigin()) {
				errorResponse.setErrorOrigin(ErrorOriginEnum.);
			}
			errorResponse.setReferenceId(ServiceContext.getCorrelationId());
			if (response instanceof HttpServletResponse) {
				HttpServletResponse hsr = (HttpServletResponse) response;
				hsr.setStatus(errorResponse.getStatusCode());
				// hsr.setStatus(HttpStatus.OK.value());
			}
			errResponse = mapper
					.writeValueAsBytes(Arrays.asList(errorResponse));
			response.getOutputStream().write(errResponse);
			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			logErrorResponse(request, response, errorResponse, e);
		}

		*//********** Un-setting thread-local object ***************//*
		ServiceContext.unsetContext();
	}
	private void retrieveErrorDtl(ErrorResponse errorResponse, Throwable cause,
			ErrorOriginEnum errorOrigin) {
		String moreInfo = null;
		if (cause instanceof ApplicationException) {
			ApplicationException applicationException = (ApplicationException) cause;
			if (applicationException != null) {
				if (applicationException.getPayloadObj() != null) {
					moreInfo = parseForErrorString(
							applicationException.getPayloadObj(),
							applicationException.getErrorOrigin());
				}
				if (applicationException.getErrorOrigin() != null) {
					errorOrigin = applicationException.getErrorOrigin();
				}
				if (StringUtils.isEmpty(moreInfo)) {
					moreInfo = extractRootErrorMsg(moreInfo,
							applicationException);
				}
				applicationException.setPayloadObj(moreInfo);
			}
		} else if (cause instanceof SystemException) {
			SystemException systemException = (SystemException) cause;
			if (systemException != null) {
				if (systemException.getPayloadObj() != null) {
					moreInfo = parseForErrorString(
							systemException.getPayloadObj(),
							systemException.getErrorOrigin());
				}
				if (systemException.getErrorOrigin() != null) {
					errorOrigin = systemException.getErrorOrigin();
				}
				if (StringUtils.isEmpty(moreInfo)) {
					moreInfo = extractRootErrorMsg(moreInfo, systemException);
				}
				systemException.setPayloadObj(moreInfo);
			}
		}
		errorResponse.setErrorOrigin(errorOrigin);
		errorResponse.setMoreInfo(moreInfo);
	}

	private String parseForErrorString(Object payloadObj,
			ErrorOriginEnum errorOrigin) {
		String newPayload = null;
		if (errorOrigin.equals(ErrorOriginEnum.)) {
			if (payloadObj instanceof String) {
				String payloadJson = (String) payloadObj;
				Map<String, Object> jsonMap = JacksonMapper
						.convertToGenericMap(payloadJson);
				newPayload = renderMapJsonData(jsonMap,
						ApplicationConstants._ERROR_NODE);
				if (newPayload == null
						&& StringUtils.isEmpty((String) newPayload)) {
					List<Map<String, Object>> jsonMaps = JacksonMapper
							.convertToListOfMap(payloadJson);
					newPayload = renderListJsonData(jsonMaps,
							ApplicationConstants._ERROR_NODE);
				}
			} else if (payloadObj instanceof Map) {
				newPayload = renderMapJsonData((Map<String, Object>) payloadObj,
						ApplicationConstants._ERROR_NODE);
			} else if (payloadObj instanceof List) {
				newPayload = renderListJsonData(
						(List<Map<String, Object>>) payloadObj,
						ApplicationConstants._ERROR_NODE);
			}
		}
		if (newPayload == null) {
			if (payloadObj instanceof String) {
				newPayload = (String) payloadObj;
			} else {
				newPayload = JacksonMapper.convertToJson(payloadObj);
			}
		}
		return newPayload;
	}

	private String renderMapJsonData(Map<String, Object> payload,
			String nodeName) {
		if (MapUtils.isNotEmpty(payload) && payload.containsKey(nodeName)) {
			return JacksonMapper.convertToJson(payload.get(nodeName));
		}
		return JacksonMapper.convertToJson(payload);
	}

	private String renderListJsonData(List<Map<String, Object>> jsonMaps,
			String nodeName) {
		if (CollectionUtils.isNotEmpty(jsonMaps)) {
			Map<String, Object> jsonMap = jsonMaps.get(0);
			if (MapUtils.isNotEmpty(jsonMap) && jsonMap.containsKey(nodeName)) {
				return JacksonMapper.convertToJson(jsonMap.get(nodeName));
			}
			return JacksonMapper.convertToJson(jsonMaps);
		}
		return null;
	}

	private void logErrorResponse(ServletRequest request,
			ServletResponse response, ErrorResponse err, Exception e)
			throws JsonProcessingException {
		String url = null;
		Integer statusCode = null;
		Map<String, String> respHeaders = null;
		String payload = null;
		if (request instanceof HttpServletRequest) {
			HttpServletRequest hsr = (HttpServletRequest) request;
			url = hsr.getRequestURL().toString() + "?" + hsr.getQueryString();
		}
		if (response instanceof HttpServletResponse) {
			HttpServletResponse hsre = (HttpServletResponse) response;
			statusCode = hsre.getStatus();
			respHeaders = LogServletFilter.getHttpResParams(hsre);
		}
		
		 * if (logPayload || logger.isDebugEnabled()) { payload =
		 * JacksonMapper.convertToJson(e); } else { payload =
		 * ApplicationConstants.DEFAULT_LOG_PAYLOAD; }
		 
		SystemLogMessage systemLogMessage = new SystemLogMessage(
				SystemLogMessage.EXITING, url, statusCode.toString(),
				respHeaders, payload);
		StringBuilder stringBuilder = new StringBuilder();
		if (e != null) {
			stringBuilder.append(systemLogMessage.toString())
					.append("|ExceptionStackTrace: ")
					.append(ExceptionUtils.getStackTrace(e));
		}
		String errorAsLogStr = systemLogMessage.errJsonMapStr(e);
		LoggerComponent LoggerComponent = new LoggerComponent(
				logStoreEnv);
		LoggerComponent.pushSysLogsTo(errorAsLogStr);
		logger.error(stringBuilder.toString());
	}

	private String extractRootErrorMsg(String moreInfo, Exception exception) {
		Exception rootException = null;
		if (exception instanceof ApplicationException) {
			rootException = ((ApplicationException) exception)
					.getRootException();
		} else if (exception instanceof SystemException) {
			rootException = ((SystemException) exception).getRootException();
		}

		if (rootException != null) {
			moreInfo = rootException.getMessage();
		} else {
			moreInfo = exception.getMessage();
		}
		return moreInfo;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}
}
*/