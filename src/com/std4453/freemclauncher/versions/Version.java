package com.std4453.freemclauncher.versions;

public class Version {
	protected String versionName;

	protected ReleaseType releaseType;
	protected VersionType versionType;
	protected long buildTime;
	protected long releaseTime;
	
	public Version(String versionName, ReleaseType releaseType,
			VersionType versionType, long buildTime) {
		this(versionName, releaseType, versionType, buildTime, buildTime);
	}

	public Version(String versionName, ReleaseType releaseType,
			VersionType versionType, long buildTime, long releaseTime) {
		this.versionName = versionName;
		this.releaseType = releaseType;
		this.versionType = versionType;
		this.buildTime = buildTime;
		this.releaseTime = releaseTime;
	}

	public long getBuildTime() {
		return buildTime;
	}

	public long getReleaseTime() {
		return releaseTime;
	}

	public ReleaseType getReleaseType() {
		return releaseType;
	}

	public String getVersionName() {
		return versionName;
	}

	public VersionType getVersionType() {
		return versionType;
	}

	@Override
	public String toString() {
		return "Version [versionName=" + versionName + ", releaseType="
				+ releaseType + ", versionType=" + versionType + ", buildTime="
				+ buildTime + ", releaseTime=" + releaseTime + "]";
	}
}
