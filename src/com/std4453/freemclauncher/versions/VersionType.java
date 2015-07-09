package com.std4453.freemclauncher.versions;

public enum VersionType {
	/**
	 * for minecraft ver. 1.5 and older
	 */
	OLD,
	/**
	 * for minecraft ver. 1.6+
	 */
	NEW;

	public static VersionType fromString(String name) {
		return name == null || !name.toLowerCase().equals("new") ? OLD : NEW;
	}
}
