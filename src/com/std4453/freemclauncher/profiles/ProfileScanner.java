package com.std4453.freemclauncher.profiles;

import java.io.File;
import java.util.List;
import java.util.Vector;

import com.std4453.freemclauncher.files.DirectoryHelper;

public class ProfileScanner {
	protected List<Profile> profiles;

	public ProfileScanner() {
		this.profiles = new Vector<Profile>();
	}

	public void scanForProfiles() {
		this.profiles.clear();

		File versionsRoot = DirectoryHelper.versions;
		if (!versionsRoot.exists())
			return;
		String[] versionNames = versionsRoot.list();

		File versionFile;
		for (String versionName : versionNames) {
			versionFile = new File(versionsRoot, versionName);

			if (versionFile != null && versionFile.exists()
					&& versionFile.isDirectory())
				scanForProfile(versionName, versionFile);
		}
	}

	protected void scanForProfile(String profileName, File profileRoot) {
		File profileJSON = new File(profileRoot, profileName + ".json");
		File profileJAR = new File(profileRoot, profileName + ".jar");
		if (profileJSON.isDirectory() || profileJAR.isDirectory()
				|| !profileJSON.exists() || !profileJAR.exists())
			return;

		Profile profile = new Profile(profileName, profileJSON, profileJAR);
		if (profile.isAvailable())
			this.profiles.add(profile);
	}

	public List<Profile> getProfiles() {
		return profiles;
	}

	public static void main(String[] args) {
		new ProfileScanner().scanForProfiles();
	}
}
