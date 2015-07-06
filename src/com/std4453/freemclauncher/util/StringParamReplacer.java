package com.std4453.freemclauncher.util;

public class StringParamReplacer {
	public static String replaceParam(String original, String paramName,
			String paramValue) {
		return original.replace("${"+paramName+"}", paramValue);
	}
}
