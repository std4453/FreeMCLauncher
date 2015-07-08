package com.std4453.freemclauncher.util;

public class StringParamReplacer {
	public static String replaceParam(String original, String paramName,
			String paramValue) {
		if (paramValue == null || paramName == null)
			return original;
		return original.replace("${" + paramName + "}", paramValue);
	}
}
