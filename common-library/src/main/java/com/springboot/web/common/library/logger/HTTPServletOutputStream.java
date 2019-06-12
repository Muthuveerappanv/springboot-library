package com.springboot.web.common.library.logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

public class HTTPServletOutputStream extends ServletOutputStream
{
	private ByteArrayOutputStream baos = new ByteArrayOutputStream();
	private ServletOutputStream originalOutputStream = null;

	public HTTPServletOutputStream(ServletOutputStream outputStream) 
	{
		this.originalOutputStream = outputStream;
	}

	@Override
	public boolean isReady() 
	{
		return false;
	}

	@Override
	public void setWriteListener(WriteListener listener)
	{
	}

	@Override
	public void write(int b) throws IOException
	{
		baos.write(b);
		originalOutputStream.write(b);
	}

	public String getPayload()
	{
		return baos.toString();
	}
}
