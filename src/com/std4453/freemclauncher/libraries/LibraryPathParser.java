package com.std4453.freemclauncher.libraries;

import java.util.regex.Pattern;

import com.std4453.freemclauncher.util.StringParamReplacer;

public class LibraryPathParser {
	public static String getLibraryPath(String libraryName) {
		String[] splitted = libraryName.split(Pattern.quote(":"));
		String packageName = splitted[0];
		String libraryRealName = splitted[1];
		String libraryVersion = splitted[2];
		String packagePath = packageName.replace('.', '/');
		return String.format("%s/%s/%s/%s-%s.jar", packagePath,
				libraryRealName, libraryVersion, libraryRealName,
				libraryVersion);
	}

	public static String getNativeName(String libraryName, String platformString) {
		String[] splitted = libraryName.split(Pattern.quote(":"));
		String packageName = splitted[0];
		String libraryRealName = splitted[1];
		String libraryVersion = splitted[2];
		String packagePath = packageName.replace('.', '/');

		String platformStringReplaced = StringParamReplacer.replaceParam(
				platformString, "arch",
				System.getProperty("os.arch").equals("x86") ? "32" : "64");

		return String.format("%s/%s/%s/%s-%s-%s.jar", packagePath,
				libraryRealName, libraryVersion, libraryRealName,
				libraryVersion, platformStringReplaced);
	}
}
