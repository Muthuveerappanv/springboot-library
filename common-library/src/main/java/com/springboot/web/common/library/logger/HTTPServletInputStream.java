package com.springboot.web.common.library.logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

public class HTTPServletInputStream extends ServletInputStream
{
	private ByteArrayInputStream bais = null;
	
	public HTTPServletInputStream(byte[] payload) 
	{
		bais = new ByteArrayInputStream(payload);
	}

	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isReady() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setReadListener(ReadListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int read() throws IOException {
		// TODO Auto-generated method stub
		return bais.read();
	}
}
