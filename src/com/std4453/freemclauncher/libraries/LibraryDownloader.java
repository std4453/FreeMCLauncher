package com.std4453.freemclauncher.libraries;

import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.std4453.freemclauncher.files.DirectoryHelper;
import com.std4453.freemclauncher.files.FileHelper;
import com.std4453.freemclauncher.net.IServerManager;
import com.std4453.freemclauncher.net.Server;
import com.std4453.freemclauncher.net.ServerManagerFactory;
import com.std4453.freemclauncher.util.LazyFileOutputStream;
import com.std4453.freemclauncher.versions.Version;

public class LibraryDownloader {
	public LibraryDownloader() {
	}

	public Object[] getLibraryDownloadEntries(Library library) {
		String libraryPath = library.path;
		String libraryNatives = library.natives;

		Map<String, String> data = new HashMap<String, String>();
		data.put("libraryPath", libraryPath);
		IServerManager serverManager = ServerManagerFactory.getServerManager();
		String libraryUrl = serverManager.getDownloadURL(
				Server.LIBRARIES_SERVER, data);
		File libraryFile = new File(DirectoryHelper.libraries, libraryPath);
		if (libraryFile.exists()) {
			libraryUrl = null;
			libraryFile = null;
		}
		OutputStream libraryOutputStream = new LazyFileOutputStream(libraryFile);

		if (libraryNatives != null) {
			data.put("libraryPath", libraryNatives);
			String libraryNativesUrl = serverManager.getDownloadURL(
					Server.LIBRARIES_SERVER, data);
			File libraryNativesFile = new File(DirectoryHelper.libraries,
					libraryNatives);
			if (libraryNativesFile.exists()) {
				libraryNativesUrl = null;
				libraryNativesFile = null;
			}

			OutputStream libraryNativesOutputStream = new LazyFileOutputStream(
					libraryNativesFile);

			return new Object[] { libraryUrl, libraryOutputStream,
					libraryNativesUrl, libraryNativesOutputStream };
		}

		return new Object[] { libraryUrl, libraryOutputStream };
	}

	public Map<String, OutputStream> getLibrariesDownloadMapping(Version version) {
		Map<String, OutputStream> mapping = new HashMap<String, OutputStream>();

		List<Library> libraries = new LibraryIndexer()
				.getLibrariesOfVersion(version.getVersionName());

		Object[] libraryEntries;

		for (Library library : libraries) {
			libraryEntries = getLibraryDownloadEntries(library);

			putEntries(mapping, libraryEntries);
		}

		return mapping;
	}

	protected void putEntries(Map<String, OutputStream> mapping,
			Object[] entries) {
		String url=null;
		for (int i = 0; i < entries.length; ++i) {
			if (entries[i] == null)
				continue;
			if (i % 2 == 0)
				url = (String) entries[i];
			else {
				mapping.put(url, (OutputStream) entries[i]);
			}
		}
	}
}
