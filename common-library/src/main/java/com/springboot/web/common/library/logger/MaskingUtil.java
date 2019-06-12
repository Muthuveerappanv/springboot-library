package com.springboot.web.common.library.logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MaskingUtil {
	
	private static Pattern jsonMaskPattern = null;
	private static Pattern genMaskPattern = null;

	public static String maskPaylod(String message) {
		try {
			if(jsonMaskPattern == null || genMaskPattern == null) {
				setMaskingPatterns();
			}
			if (jsonMaskPattern!= null && genMaskPattern!= null) {
				Matcher matcher1 = jsonMaskPattern.matcher(message);
				String jsonMasked =  matcher1.replaceAll("$1\"*********\"");

				Matcher matcher2 = genMaskPattern.matcher(jsonMasked);
				return matcher2.replaceAll("$1*********$5");
			}
		} catch (Exception e) {
			return message;
		}
		return message;
	}
	
	private static void setMaskingPatterns() {
		//String maskableEntries = ConfigHelper.getStringProp(ApplicationConstants.MASKABLE_KEYS);
		/*if (StringUtils.isNotEmpty(maskableEntries) && !maskableEntries.equalsIgnoreCase("default")) {
			String jsonPattern = "(?i)(\"(" + maskableEntries + ")\":)(([.*?])|(\".*?\"))";
			jsonMaskPattern = Pattern.compile(jsonPattern);

			String regularMask = "(?i)((password|access_token)=)(([\\S])+?)(\\||&|\n)";
			genMaskPattern = Pattern.compile(regularMask);
		}*/
	}
}
