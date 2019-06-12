package com.springboot.web.common.library.logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import com.springboot.web.common.library.util.ApplicationConstants;
import com.springboot.web.common.library.util.CustomJacksonMapper;

public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private Boolean logPayload = false;

	public LoggingRequestInterceptor(Boolean logPayload) {
		this.logPayload = logPayload;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body,
			ClientHttpRequestExecution execution) {
		ClientHttpResponse response = null;
		Long currentTime = System.currentTimeMillis();
		try {
			traceRequest(request, body);
			response = execution.execute(request, body);
			traceResponse(response, request.getURI(), currentTime,
					request.getMethod().name());
		} catch (IOException exception) {
			logger.error("IO Exception on executing REST call!", exception);
		}

		return response;
	}

	private void traceRequest(HttpRequest request, byte[] body)
			throws IOException {
		String stringBody = ApplicationConstants.DEFAULT_LOG_PAYLOAD;
		Map<String, String> requestHdrs = null;
		String httpMethod = request.getMethod().name();
		if (logPayload) {
			requestHdrs = request.getHeaders().toSingleValueMap();
			if (body != null) {
				stringBody = CustomJacksonMapper.converToSingleLineString(
						new String(body, StandardCharsets.UTF_8));
			}
		}
		SystemLogMessage systemLogMessage = new SystemLogMessage(
				SystemLogMessage.REST_TEMPLATE_RQST,
				request.getURI().toString(), requestHdrs, stringBody,
				httpMethod);
		logger.info(systemLogMessage.toString());
	}

	private void traceResponse(ClientHttpResponse response, URI uri,
			Long currentTime, String httpMethod) throws IOException {
		StringBuilder inputStringBuilder = new StringBuilder();
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(response.getBody(),
						StandardCharsets.UTF_8));
		String line = bufferedReader.readLine();
		Map<String, String> responseHdrs = null;
		while (line != null) {
			inputStringBuilder.append(line);
			inputStringBuilder.append('\n');
			line = bufferedReader.readLine();
		}
		String stringBody = ApplicationConstants.DEFAULT_LOG_PAYLOAD;
		if (logPayload) {
			responseHdrs = response.getHeaders().toSingleValueMap();
			if (StringUtils.isNoneEmpty(inputStringBuilder.toString())) {
				stringBody = CustomJacksonMapper.converToSingleLineString(
						inputStringBuilder.toString());
			}
		}
		SystemLogMessage systemLogMessage = new SystemLogMessage(
				SystemLogMessage.REST_TEMPLATE_RESP, uri.toString(),
				response.getStatusCode().toString(), responseHdrs, stringBody,
				currentTime, httpMethod);
		logger.info(systemLogMessage.toString());
	}

}