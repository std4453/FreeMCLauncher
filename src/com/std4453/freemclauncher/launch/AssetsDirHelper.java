package com.std4453.freemclauncher.launch;

import java.io.File;

import org.json.JSONObject;

import com.std4453.freemclauncher.files.DirectoryHelper;
import com.std4453.freemclauncher.files.FileHelper;
import com.std4453.freemclauncher.profiles.Profile;
import com.std4453.freemclauncher.util.StructuredDataHelper;
import com.std4453.freemclauncher.util.StructuredDataObject;

public class AssetsDirHelper {
	public static String getAssetsDir(Profile profile) {
		String name = profile.getName();
		File jsonFile = new File(DirectoryHelper.versions, String.format(
				"%s/%s.json", name, name));
		String jsonString = FileHelper.getFileContentAsString(jsonFile);
		JSONObject jsonObj = new JSONObject(jsonString);
		StructuredDataObject jsonSDO = StructuredDataHelper.fromJSONObject(
				jsonObj).toStructuredDataObject();

		int launcherVersion = jsonSDO.getInt("minimumLauncherVersion");

		if (launcherVersion > 10)
			return DirectoryHelper.assets.getAbsolutePath();
		else
			return new File(DirectoryHelper.assets, "virtual/legacy")
					.getAbsolutePath();
	}
}
