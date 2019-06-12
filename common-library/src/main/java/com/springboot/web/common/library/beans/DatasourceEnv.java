package com.springboot.web.common.library.beans;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@ConfigurationProperties(prefix = "library.database")
@Component
public @Data class DatasourceEnv {

	@Value("true")
	private Boolean required;
	private String jdbcUrl;
	private String username;
	private String password;
	private String driverClassName;
	@Value("100")
	private Integer maxActive;
	@Value("10000")
	private Integer maxWait;
	@Value("true")
	private Boolean testOnBorrow;

}
