package com.std4453.freemclauncher.launch;

import java.io.File;

import org.json.JSONObject;

import com.std4453.freemclauncher.files.DirectoryHelper;
import com.std4453.freemclauncher.files.FileHelper;
import com.std4453.freemclauncher.profiles.Profile;
import com.std4453.freemclauncher.util.StructuredDataHelper;
import com.std4453.freemclauncher.util.StructuredDataObject;

public class MainClassHelper {
	public static String getMinecraftMainClass(Profile profile) {
		String versionName = profile.getName();
		File file = new File(DirectoryHelper.versions, versionName + "/"
				+ versionName + ".json");

		String content = FileHelper.getFileContentAsString(file);
		JSONObject obj = new JSONObject(content);
		StructuredDataObject sdo = StructuredDataHelper.fromJSONObject(obj)
				.toStructuredDataObject();
		
		return sdo.getString("mainClass");
	}
}
