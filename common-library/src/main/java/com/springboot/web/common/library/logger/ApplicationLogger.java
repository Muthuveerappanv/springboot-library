package com.springboot.web.common.library.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationLogger {

	public ApplicationLogger() {
		super();
	}

	Logger logger = null;

	public void logInfo(Object object, String logMessage, Object payload) {
		logger = (Logger) LoggerFactory.getLogger(object.getClass());
		try {
			ApplicationLogMessage alp = new ApplicationLogMessage(
					retrieveClsNm(object), retrieveMethodNm(object), logMessage,
					payload);
			logger.info(alp.toString());
		} catch (Exception e) {
			logger.error("Error in ApplicationLogger - logInfo", e);
		}
	}

	public void logError(Object object, String logMessage,
			Exception exception) {
		logger = LoggerFactory.getLogger(object.getClass());
		try {
			ApplicationLogMessage alp = new ApplicationLogMessage(
					retrieveClsNm(object), retrieveMethodNm(object), logMessage,
					exception);
			logger.error(alp.toString());
		} catch (Exception e) {
			logger.error("Error in ApplicationLogger - logError", e);
		}
	}

	public void logDebug(Object object, String logMessage, Object payload) {
		logger = LoggerFactory.getLogger(object.getClass());
		try {
			ApplicationLogMessage alp = new ApplicationLogMessage(
					retrieveClsNm(object), retrieveMethodNm(object), logMessage,
					payload);
			logger.debug(alp.toString());
		} catch (Exception e) {
			logger.error("Error in ApplicationLogger - logDebug", e);
		}
	}

	private String retrieveMethodNm(Object object) {
		try {
			return Thread.currentThread().getStackTrace()[3].getMethodName();
		} catch (Exception e) {
			return "";
		}
	}

	public String retrieveClsNm(Object object) {
		if (object != null) {
			if (object instanceof Class<?>) {
				return object.toString();
			} else {
				return object.getClass().getName();
			}
		}
		return null;
	}
}
