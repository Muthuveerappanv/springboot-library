package com.springboot.web.common.library.exception;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.ResponseErrorHandler;

import com.springboot.web.common.library.util.ApplicationConstants;

public class CustomErrorRespHandler implements ResponseErrorHandler {

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		HttpStatus statusCode = getHttpStatusCode(response);
		return (statusCode.series() == HttpStatus.Series.CLIENT_ERROR
				|| statusCode.series() == HttpStatus.Series.SERVER_ERROR);
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		HttpStatus statusCode = getHttpStatusCode(response);
		if (statusCode.series() == HttpStatus.Series.CLIENT_ERROR
				|| statusCode.series() == HttpStatus.Series.SERVER_ERROR) {
			throw new SystemException(
					ApplicationConstants.REST_CLIENT_EXCEPTION,
					getResponseBody(response));
		}
	}

	private HttpStatus getHttpStatusCode(ClientHttpResponse response)
			throws IOException {
		HttpStatus statusCode;
		if (null == response || null == response.getStatusCode()) {
			throw new SystemException(
					ApplicationConstants.REST_CLIENT_EXCEPTION);
		}
		try {
			statusCode = response.getStatusCode();
		} catch (IllegalArgumentException ex) {
			throw new SystemException(
					ApplicationConstants.REST_CLIENT_EXCEPTION, ex);
		}
		return statusCode;
	}

	private byte[] getResponseBody(ClientHttpResponse response) {
		try {
			InputStream responseBody = response.getBody();
			if (responseBody != null) {
				return FileCopyUtils.copyToByteArray(responseBody);
			}
		} catch (IOException ex) {
			// ignore
		}
		return new byte[0];
	}

}
