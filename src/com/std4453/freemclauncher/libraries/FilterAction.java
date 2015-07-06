package com.std4453.freemclauncher.libraries;

public enum FilterAction {
	ALLOW,DISALLOW;
	
	public static FilterAction fromString(String name) {
		return name.equals("allow")?ALLOW:DISALLOW;
	}
	
	public static boolean toBoolean(FilterAction action) {
		return action==ALLOW?true:false;
	}
}
