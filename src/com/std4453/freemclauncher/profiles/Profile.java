package com.std4453.freemclauncher.profiles;

import static com.std4453.freemclauncher.logging.Logger.*;

import java.io.File;
import java.io.OutputStream;
import java.util.Map;

import org.json.JSONObject;

import com.std4453.freemclauncher.files.FileHelper;
import com.std4453.freemclauncher.gui.DownloaderMultiple;
import com.std4453.freemclauncher.libraries.LibraryDownloader;
import com.std4453.freemclauncher.util.StructuredDataHelper;
import com.std4453.freemclauncher.util.StructuredDataObject;

public class Profile {
	private static final LibraryDownloader libraryDownloader = new LibraryDownloader();

	protected String name;
	protected String id;

	protected File profileJSON;
	protected File profileJAR;

	protected boolean available;

	public Profile(String name, File profileJSON, File profileJAR) {
		this.name = name;
		this.profileJSON = profileJSON;
		this.profileJAR = profileJAR;

		try {
			String jsonStr = FileHelper.getFileContentAsString(profileJSON);
			JSONObject jsonObj = new JSONObject(jsonStr);
			StructuredDataObject sdo = StructuredDataHelper.fromJSONObject(
					jsonObj).toStructuredDataObject();
			id = sdo.getString("id");
		} catch (Throwable t) {
			log(ERROR, "Corrupted or unavailable profile " + name);
			log(ERROR, t);
			available = false;
		}

		available = true;
	}

	public File getProfileJSON() {
		return profileJSON;
	}

	public File getProfileJAR() {
		return profileJAR;
	}

	public boolean isAvailable() {
		return available;
	}

	public String getName() {
		return name;
	}

	public void checkLibraryAvailability() {
		Map<String, OutputStream> mapping = libraryDownloader
				.getLibrariesDownloadMapping(name);
		if (mapping == null || mapping.size() == 0 || mapping.isEmpty())
			return;

		DownloaderMultiple downloader = new DownloaderMultiple(mapping);
		downloader.getFrame().setVisible(true);
		downloader.run();
		downloader.getFrame().dispose();
	}

	public String getId() {
		return id;
	}
}
