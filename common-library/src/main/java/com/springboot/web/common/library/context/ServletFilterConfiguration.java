package com.springboot.web.common.library.context;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

import com.springboot.web.common.library.logger.LogServletFilter;

@Configuration
public class ServletFilterConfiguration {

	@Autowired
	private ConfigurableEnvironment env;
	
	private Boolean isLogPayload = false;

	private Logger logger = LoggerFactory
			.getLogger(ServletFilterConfiguration.class);

	@PostConstruct
	public void postCons() {
		String activeProfiles = StringUtils.join(env.getActiveProfiles());
		if (StringUtils.isBlank(activeProfiles)
				|| activeProfiles.contains("default")
				|| activeProfiles.contains("dev")
				|| activeProfiles.contains("qf") || logger.isDebugEnabled()) {
			isLogPayload = true;
		}
		/*logStoreEnv.setAppName(
				env.getProperty(ApplicationConstants.SPRING_APP_NM));
		logStoreEnv.setAppVersion(
				env.getProperty(ApplicationConstants.SPRING_APP_VER));*/
	}

	/*@Bean(name = "exceptionServletFilter")
	FilterRegistrationBean addExceptionFilter() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new ExceptionServletFilter(exceptionMapper,
				isLogPayload, logStoreEnv, marketMap));
		registration.addUrlPatterns("/*");
		registration.setName("exceptionServletFilter");
		registration.setOrder(1);
		return registration;
	}*/

	@Bean(name = "logServletFilter")
	FilterRegistrationBean<LogServletFilter> addLogFilter() {
		FilterRegistrationBean<LogServletFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(new LogServletFilter(isLogPayload));
		registration.addUrlPatterns("/*");
		registration.setName("logServletFilter");
		registration.setOrder(1);
		return registration;
	}
	
}
