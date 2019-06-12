package com.springboot.web.common.library.logger;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class MaskingPatternLayout extends PatternLayout {

	@Override
	public String doLayout(ILoggingEvent event) {
		final StringBuilder message = new StringBuilder(super.doLayout(event));
		return MaskingUtil.maskPaylod(message.toString());
	}
}