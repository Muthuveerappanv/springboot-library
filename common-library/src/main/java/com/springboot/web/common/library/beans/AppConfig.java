package com.springboot.web.common.library.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import lombok.Data;
@Data
@Configuration
@ConfigurationProperties(prefix = "library.timeout")
public class AppConfig {
	@Value("30000")
	private Integer connTimeOut;
	@Value("30000")
	private Integer readTimeOut;
	@Value("5")
	private Integer retryAttempt;
	@Value("1000")
	private Integer retryTimegap;
	@Autowired
	private RestTemplate restTemplate;

	@Bean
	public RestTemplate customRestTemplate() {
		SimpleClientHttpRequestFactory s = new SimpleClientHttpRequestFactory();
		//s.setConnectTimeout(connTimeOut);
		// s.setReadTimeout(readTimeOut);
		s.setOutputStreaming(false);
		restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(s));

		return restTemplate;
	}

	/*@Bean
	public RetryTemplate retryTemplate() {
		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		retryPolicy.setMaxAttempts(retryAttempt);

		FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
		backOffPolicy.setBackOffPeriod(retryTimegap);

		RetryTemplate template = new RetryTemplate();
		template.setRetryPolicy(retryPolicy);
		template.setBackOffPolicy(backOffPolicy);

		return template;
	}*/
}
