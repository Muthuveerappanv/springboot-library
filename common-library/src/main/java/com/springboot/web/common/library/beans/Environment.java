package com.springboot.web.common.library.beans;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
public @Data class Environment {

	// Service Name and Version
	@Value("${library.service.name:${spring.application.name:default-application}}")
	private String serviceName;
	@Value("${library.service.version:${spring.application.version:v1}}")
	private String serviceVersion;
	@Value("${library.service.version:${spring.application.mavenVersion:1.0.0}}")
	private String serviceMavenVersion;
	
	// Exception Handling Framework Properties
	@Value("${library.exception.defaultStatusCode:500}")
	private int defaultExceptionStatusCode;
	@Value("${library.exception.defaultErrorCode:SYS_ERR_DEF}")
	private String defaultExceptionErrorCode;
	@Value("${library.exception.defaultErrorMessage:'System default error! Please contact technical support'}")
	private String defaultExceptionErrorMessage;
	@Value("${library.exception.tableName:API.EXCPTN_CONFIG}")
	private String exceptionTableName;
	@Value("${library.exception.refreshInterval:1800000}")
	private long exceptionRefreshInterval;
}
