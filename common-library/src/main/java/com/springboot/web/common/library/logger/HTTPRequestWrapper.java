package com.springboot.web.common.library.logger;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;

import com.springboot.web.common.library.exception.ExceptionMessage;
import com.springboot.web.common.library.exception.SystemException;
import com.springboot.web.common.library.util.CustomJacksonMapper;

public class HTTPRequestWrapper extends HttpServletRequestWrapper {
	byte[] payload = null;
	HTTPServletInputStream hsis = null;

	public HTTPRequestWrapper(HttpServletRequest request) {
		super(request);
		try {
			InputStream is = request.getInputStream();
			payload = IOUtils.toByteArray(is);
			hsis = new HTTPServletInputStream(payload);
		} catch (IOException e) {
			throw new SystemException(
					ExceptionMessage.ERROR_READING_HTTP_INPUTSTREAM);
		}
	}

	public String getPayload() {

		return CustomJacksonMapper.converToSingleLineString(new String(payload));

	}

	@Override
	public ServletInputStream getInputStream() {
		return hsis;
	}
}
