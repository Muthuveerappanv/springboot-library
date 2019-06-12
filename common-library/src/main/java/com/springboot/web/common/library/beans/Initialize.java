package com.springboot.web.common.library.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.springboot.web.common.library.context.ServletFilterConfiguration;
import com.springboot.web.common.library.exception.CustomErrorRespHandler;
import com.springboot.web.common.library.logger.ApplicationLogger;
import com.springboot.web.common.library.logger.LoggingRequestInterceptor;

@Component
@Import({Environment.class, JsonMapperConfiguration.class,
		ServletFilterConfiguration.class, ApplicationLogger.class,
		AppConfig.class,})
public class Initialize {
	// Initializing Environment, CustomPooledDatsource, JsonMapperConfiguration
	// and ServletFilterConfiguration classes

	private Boolean isLogPayload = false;

	private Logger logger = LoggerFactory.getLogger(Initialize.class);

	@Autowired
	private ConfigurableEnvironment env;

	@PostConstruct
	public void postCons() {
		String activeProfiles = StringUtils.join(env.getActiveProfiles());
		if (StringUtils.isBlank(activeProfiles)
				|| activeProfiles.contains("default")
				|| activeProfiles.contains("dev")
				|| activeProfiles.contains("qf") || logger.isDebugEnabled()) {
			isLogPayload = true;
		}
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		// Configure HTTP Request Logger Intercepter
		ClientHttpRequestInterceptor ri = new LoggingRequestInterceptor(
				isLogPayload);
		List<ClientHttpRequestInterceptor> ris = new ArrayList<ClientHttpRequestInterceptor>();
		ris.add(ri);
		SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
		simpleClientHttpRequestFactory.setOutputStreaming(false);
		RestTemplate restTemplate = builder.additionalInterceptors(ris)
				.requestFactory(() -> new BufferingClientHttpRequestFactory(
						simpleClientHttpRequestFactory))
				.errorHandler(new CustomErrorRespHandler()).build();
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		mappingJackson2HttpMessageConverter.getObjectMapper()
				.setSerializationInclusion(Include.NON_NULL);
		mappingJackson2HttpMessageConverter.getObjectMapper().configure(
				DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		restTemplate.getMessageConverters().removeIf(m -> m.getClass().getName()
				.equals(MappingJackson2HttpMessageConverter.class.getName()));
		restTemplate.getMessageConverters()
				.add(mappingJackson2HttpMessageConverter);
		return restTemplate;

	}

}
