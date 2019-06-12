/*package com.springboot.web.common.library.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.springboot.web.common.library.beans.Environment;
import com.springboot.web.common.library.context.ExceptionConfig;
import com.springboot.web.common.library.enums.ErrorOriginEnum;

@Component
public class ExceptionMapper {

	@Autowired
	@Qualifier("commonJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private Environment props;

	ErrorResponse defaultErrorResponse = null;
	Date lastUpdated = new Date();
	Map<String, ErrorResponse> exceptionMap = new HashMap<String, ErrorResponse>();
	String query = null;
	long refreshInterval;

	@PostConstruct
	public void it() {
		// Create query to map with Exception
		this.refreshInterval = props.getExceptionRefreshInterval();
		defaultErrorResponse = new ErrorResponse(
				props.getDefaultExceptionStatusCode(),
				props.getDefaultExceptionErrorCode(),
				props.getDefaultExceptionErrorMessage());
		if (jdbcTemplate != null) {
			getExceptionMap();
		}
	}

	private synchronized void getExceptionMap() {
		if (jdbcTemplate != null) {
			query = new String(
					"SELECT SERVC_NM, SERVC_VER_CD, OPERTN_NM, EXCPTN_CLS_NM, EXCPTN_MSG_TXT, HTTP_STUS_CD, ERR_CD, ERR_MSG_TXT FROM "
							+ props.getExceptionTableName()
							+ " WHERE SERVC_NM IN ('ALL','"
							+ props.getServiceName() + "');");

			List<ExceptionConfig> exceptions = jdbcTemplate.query(query,
					new BeanPropertyRowMapper<>(ExceptionConfig.class));

			for (ExceptionConfig row : exceptions) {
				String key = row.getSERVC_NM() + "|" + row.getSERVC_VER_CD()
						+ "|" + row.getOPERTN_NM() + "|"
						+ row.getEXCPTN_CLS_NM() + "|"
						+ row.getEXCPTN_MSG_TXT();
				ErrorResponse errResponse = new ErrorResponse(
						row.getHTTP_STUS_CD(), row.getERR_CD(),
						row.getERR_MSG_TXT());
				exceptionMap.put(key, errResponse);
			}
			lastUpdated = new Date();
		}
	}

	public ErrorResponse mapException(Throwable cause, String serviceUrl) {
		// Map errorResponse from DB
		Date currentDate = new Date();
		if (((currentDate.getTime()
				- lastUpdated.getTime()) > refreshInterval)) {
			getExceptionMap();
		}
		ErrorResponse returnResponse = null;

		String serviceName = props.getServiceName();
		String serviceVersion = props.getServiceVersion();
		String operationName = getOperationName(serviceUrl);
		String exceptionClass = cause.getClass().getName();
		String exceptionMessage = cause.getMessage();

		String key = serviceName + "|" + serviceVersion + "|" + operationName
				+ "|" + exceptionClass + "|" + exceptionMessage;
		if (MapUtils.isNotEmpty(exceptionMap)) {
			returnResponse = exceptionMap.get(key);
		}
		if (returnResponse == null) {
			key = serviceName + "|ALL|" + operationName + "|" + exceptionClass
					+ "|" + exceptionMessage;
			returnResponse = exceptionMap.get(key);
		}
		if (returnResponse == null) {
			key = "ALL|ALL|" + operationName + "|" + exceptionClass + "|"
					+ exceptionMessage;
			returnResponse = exceptionMap.get(key);
		}
		if (returnResponse == null) {
			key = serviceName + "|" + serviceVersion + "|ALL|" + exceptionClass
					+ "|" + exceptionMessage;
			returnResponse = exceptionMap.get(key);
		}
		if (returnResponse == null) {
			key = "ALL|ALL|ALL|" + exceptionClass + "|" + exceptionMessage;
			returnResponse = exceptionMap.get(key);
		}
		if (returnResponse == null) {
			key = "ALL|ALL|ALL|ALL|" + exceptionMessage;
			returnResponse = exceptionMap.get(key);
		}
		if (returnResponse == null) {
			key = "ALL|ALL|ALL|ALL|ALL";
			returnResponse = exceptionMap.get(key);
		}
		if (returnResponse == null) {
			returnResponse = defaultErrorResponse;
		}
		return returnResponse;
	}

	private String getOperationName(String serviceUrl) {

		String operationName = "";
		String serviceUrlTokens[] = serviceUrl.split("/");

		if (serviceUrlTokens.length > 4) {
			String entireOperation = "";
			entireOperation = serviceUrlTokens[serviceUrlTokens.length - 1];

			if (entireOperation.contains("?")) {
				operationName = entireOperation.substring(0,
						entireOperation.indexOf("?"));
			} else {
				operationName = entireOperation;
			}
		}
		return operationName;
	}

	public void setExceptionMap(Map<String, ErrorResponse> exceptionMap) {
		this.exceptionMap = exceptionMap;
	}
}
*/