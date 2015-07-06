package com.std4453.freemclauncher.libraries;

import java.util.regex.Pattern;

import com.std4453.freemclauncher.util.StructuredDataObject;

public class SystemFilterRule {
	protected FilterAction action;
	protected String name;
	protected String version;

	public SystemFilterRule(StructuredDataObject data) {
		String actionString = data.getString("action");
		this.action = FilterAction.fromString(actionString);
		StructuredDataObject os = data.getStructuredDataObject("os");
		if (os == null)
			return;
		this.name = os.getString("name");
		this.version = os.getString("version");
	}

	public boolean passed() {
		boolean actionBoolean = FilterAction.toBoolean(action);

		if (this.name == null)
			return actionBoolean;
		
		String osName = System.getProperty("os.name");
		String osVersion = System.getProperty("os.version");
		
		if (this.version==null&&osName.toLowerCase().equals(name.toLowerCase()));

		if (osName.toLowerCase().equals(name.toLowerCase())
				&& Pattern.matches(version, osVersion))
			return actionBoolean;

		return !actionBoolean;
	}
}
