package com.std4453.freemclauncher.profiles;

import java.io.File;
import java.io.OutputStream;
import java.util.Map;

import com.std4453.freemclauncher.gui.DownloaderMultiple;
import com.std4453.freemclauncher.libraries.LibraryDownloader;

public class Profile {
	private static final LibraryDownloader libraryDownloader = new LibraryDownloader();

	protected String name;

	protected File profileJSON;
	protected File profileJAR;

	protected boolean available;

	public Profile(String name, File profileJSON, File profileJAR) {
		this.name = name;
		this.profileJSON = profileJSON;
		this.profileJAR = profileJAR;

		// TODO: check for corrupted files.
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
}
