/*package com.springboot.web.common.library.beans;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CustomPooledDatsource {

	@Autowired
	DatasourceEnv datsourceEnv;

	@Bean(name = "commonJdbcTemplate")
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		JdbcTemplate jdbcTemplate = null;
		jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate;
	}

	@Bean
	@Primary
	// @ConditionalOnProperty(prefix = "library.database", value = "required",
	// havingValue = "true")
	public DataSource dataSource() {
		org.apache.tomcat.jdbc.pool.DataSource dataSource = null;
		dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
		dataSource.setUrl(datsourceEnv.getJdbcUrl());
		dataSource.setUsername(datsourceEnv.getUsername());
		dataSource.setPassword(datsourceEnv.getPassword());
		dataSource.setDriverClassName(datsourceEnv.getDriverClassName());
		dataSource.setMaxActive(datsourceEnv.getMaxActive());
		dataSource.setMaxWait(datsourceEnv.getMaxWait());
		dataSource.setTestOnBorrow(datsourceEnv.getTestOnBorrow());
		// newly added for timeout exception
		dataSource.setValidationQueryTimeout(1000);
		dataSource.setValidationQuery("SELECT 1");
		dataSource.setValidationInterval(34000);
		dataSource.setRemoveAbandoned(true);
		dataSource.setRemoveAbandonedTimeout(55);
		dataSource.setTimeBetweenEvictionRunsMillis(34000);
		dataSource.setMinEvictableIdleTimeMillis(55000);
		return dataSource;
	}

}
*/