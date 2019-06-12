package com.springboot.web.common.library.context;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Context {
	private String userId;
	private String correlationId;
	private String serviceUrl;
}
