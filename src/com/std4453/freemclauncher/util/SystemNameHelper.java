package com.std4453.freemclauncher.util;

public class SystemNameHelper {
	public static String getSystemName() {
		String osName=System.getProperty("os.name");
		if (osName.toLowerCase().indexOf("windows")!=-1)
			return "windows";
		if (osName.toLowerCase().indexOf("linux")!=-1)
			return "linux";
		if (osName.toLowerCase().indexOf("osx")!=-1)
			return "osx";
		
		return "unknown";
	}
}
