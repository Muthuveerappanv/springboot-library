package com.springboot.web.common.library.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(Include.NON_NULL)
public class ErrorResponse {

	@JsonIgnore
	private Integer statusCode;
	private String errorCode;
	private String errorMessage;
	private String moreInfo;
	private String referenceId;

	public ErrorResponse(Integer statusCode, String errorCode,
			String errorMessage) {
		this.statusCode = statusCode;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
}