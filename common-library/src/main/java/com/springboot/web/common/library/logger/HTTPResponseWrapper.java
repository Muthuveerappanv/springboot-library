package com.springboot.web.common.library.logger;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.springboot.web.common.library.exception.ExceptionMessage;
import com.springboot.web.common.library.exception.SystemException;
import com.springboot.web.common.library.util.CustomJacksonMapper;

public class HTTPResponseWrapper extends HttpServletResponseWrapper {
	private HTTPServletOutputStream hsos = null;

	public HTTPResponseWrapper(HttpServletResponse response) {
		super(response);
		try {
			hsos = new HTTPServletOutputStream(response.getOutputStream());
		} catch (IOException e) {
			new SystemException(
					ExceptionMessage.ERROR_READING_HTTP_OUTPUTSTREAM);
		}
	}

	public String getPayload() {
		return CustomJacksonMapper.converToSingleLineString(hsos.getPayload());
	}

	@Override
	public ServletOutputStream getOutputStream() {
		return hsos;

	}
}
