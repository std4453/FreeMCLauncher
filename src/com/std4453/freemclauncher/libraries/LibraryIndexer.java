package com.std4453.freemclauncher.libraries;

import java.io.File;
import java.util.List;
import java.util.Vector;

import org.json.JSONObject;

import com.std4453.freemclauncher.files.DirectoryHelper;
import com.std4453.freemclauncher.files.FileHelper;
import com.std4453.freemclauncher.util.StructuredDataArray;
import com.std4453.freemclauncher.util.StructuredDataHelper;
import com.std4453.freemclauncher.util.StructuredDataObject;

public class LibraryIndexer {
	public LibraryIndexer() {
	}

	public List<Library> getLibrariesOfVersion(String versionName) {
		File file = new File(DirectoryHelper.versions, versionName + "/"
				+ versionName + ".json");
		if (!file.exists())
			return null;

		List<Library> libraries = new Vector<Library>();

		String versionInfo = FileHelper.getFileContentAsString(file);
		JSONObject jsonObj = new JSONObject(versionInfo);
		StructuredDataObject sdo = StructuredDataHelper.fromJSONObject(jsonObj)
				.toStructuredDataObject();
		StructuredDataArray librariesSDA = sdo
				.getStructuredDataArray("libraries");

		for (Object object : librariesSDA) {
			if (object instanceof StructuredDataObject)
				libraries.add(new Library((StructuredDataObject) object));
			else
				continue;
		}

		return libraries;
	}
}
