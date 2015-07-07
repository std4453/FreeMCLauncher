package com.std4453.freemclauncher.libraries;

import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.std4453.freemclauncher.files.DirectoryHelper;
import com.std4453.freemclauncher.net.IServerManager;
import com.std4453.freemclauncher.net.Server;
import com.std4453.freemclauncher.net.ServerManagerFactory;
import com.std4453.freemclauncher.util.LazyFileOutputStream;
import com.std4453.freemclauncher.versions.Version;

public class LibraryDownloader {
	public LibraryDownloader() {
	}

	public Object[] getLibraryDownloadEntry(Library library) {
		String libraryPath = library.path;
		String libraryNatives = library.natives;

		if (!library.passed())
			return new Object[] { null, null };

		IServerManager serverManager = ServerManagerFactory.getServerManager();
		Map<String, String> data = new HashMap<String, String>();
		if (libraryNatives != null) {
			if (libraryNatives.isEmpty())
				return new Object[] { null, null };
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

			return new Object[] { libraryNativesUrl, libraryNativesOutputStream };
		} else {
			data.put("libraryPath", libraryPath);
			String libraryUrl = serverManager.getDownloadURL(
					Server.LIBRARIES_SERVER, data);
			File libraryFile = new File(DirectoryHelper.libraries, libraryPath);
			if (libraryFile.exists()) {
				libraryUrl = null;
				libraryFile = null;
			}
			OutputStream libraryOutputStream = new LazyFileOutputStream(
					libraryFile);

			return new Object[] { libraryUrl, libraryOutputStream };
		}
	}

	public Map<String, OutputStream> getLibrariesDownloadMapping(Version version) {
		Map<String, OutputStream> mapping = new HashMap<String, OutputStream>();

		List<Library> libraries = new LibraryIndexer()
				.getLibrariesOfVersion(version.getVersionName());

		Object[] libraryEntry;

		for (Library library : libraries) {
			libraryEntry = getLibraryDownloadEntry(library);

			if (libraryEntry[0] != null)
				mapping.put((String) libraryEntry[0],
						(OutputStream) libraryEntry[1]);
		}

		return mapping;
	}
}
