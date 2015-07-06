package com.std4453.freemclauncher.versions;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.std4453.freemclauncher.files.DirectoryHelper;
import com.std4453.freemclauncher.files.FileHelper;
import com.std4453.freemclauncher.net.IServerManager;
import com.std4453.freemclauncher.net.Server;
import com.std4453.freemclauncher.net.ServerManagerFactory;
import com.std4453.freemclauncher.util.LazyFileOutputStream;

public class VersionJSONDownloader {
	private static final Logger logger = Logger
			.getLogger("com.std4453.freemclauncher.log.Logger");

	public VersionJSONDownloader() {
	}

	public void downloadVersionJSON(Version version) {
		if (version == null || version.getVersionType() != VersionType.NEW)
			return;

		byte[] versionJSONByteArray = null;

		Map<String, String> downloadParams = new HashMap<String, String>();
		downloadParams.put("version", version.getVersionName());
		downloadParams.put("fileType", "JSON");
		IServerManager serverManager = ServerManagerFactory.getServerManager();
		try {
			versionJSONByteArray = serverManager.downloadFromServer(
					Server.MINECRAFT_DOWNLOAD_SERVER, downloadParams);
		} catch (Exception e) {
			logger.log(Level.WARNING,
					"Exception caught while loading new version JSON.", e);
			return;
		}

		File file = new File(DirectoryHelper.versions, String.format(
				"%s/%s.json", version.getVersionName(),
				version.getVersionName()));
		OutputStream fos = FileHelper.newOutputStream(file);
		try {
			fos.write(versionJSONByteArray);
			fos.close();
		} catch (IOException e) {
			logger.log(Level.WARNING,
					"IOException while writing version JSON data to file.", e);
		}
	}

	public Object[] getVersionJSONDownloadEntry(Version version) {
		if (version == null || version.getVersionType() != VersionType.NEW)
			return null;

		Map<String, String> downloadParams = new HashMap<String, String>();
		downloadParams.put("version", version.getVersionName());
		downloadParams.put("fileType", "JSON");
		IServerManager serverManager = ServerManagerFactory.getServerManager();
		String url = serverManager.getDownloadURL(
				Server.MINECRAFT_DOWNLOAD_SERVER, downloadParams);
		OutputStream os = new LazyFileOutputStream(new File(
				DirectoryHelper.versions, String.format("%s/%s.json",
						version.getVersionName(), version.getVersionName())));

		return new Object[] { url, os };
	}
}
