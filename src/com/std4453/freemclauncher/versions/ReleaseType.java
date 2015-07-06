package com.std4453.freemclauncher.versions;

public enum ReleaseType {
	SNAPSHOT, RELEASE, OLD_BETA, OLD_ALPHA, CLASSIC, PRE_CLASSIC;

	public static final ReleaseType fromString(String id, String name) {
		if (name.equals("release"))
			return RELEASE;
		if (name.equals("snapshot"))
			return SNAPSHOT;
		if (name.equals("old_beta"))
			return OLD_BETA;
		if (name.equals("old_alpha")) {
			if (id.startsWith("a"))
				return OLD_ALPHA;
			if (id.startsWith("rd"))
				return PRE_CLASSIC;
			return CLASSIC;
		}
		
		return null;
	}
}
