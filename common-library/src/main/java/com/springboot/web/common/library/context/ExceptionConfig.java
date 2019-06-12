package com.springboot.web.common.library.context;

import lombok.Data;

public @Data class ExceptionConfig {

	private String SERVC_NM;
	private String SERVC_VER_CD;
	private String OPERTN_NM;
	private String EXCPTN_CLS_NM;
	private String EXCPTN_MSG_TXT;
	private Integer HTTP_STUS_CD;
	private String ERR_CD;
	private String ERR_MSG_TXT;

}