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

public class VersionDownloader {
	private static final Logger logger = Logger
			.getLogger("com.std4453.freemclauncher.log.Logger");

	public VersionDownloader() {
	}

	public void downloadVersionJAR(Version version) {
		if (version == null || version.getVersionType() != VersionType.NEW)
			return;

		byte[] versionJARByteArray = null;

		Map<String, String> downloadParams = new HashMap<String, String>();
		downloadParams.put("version", version.getVersionName());
		downloadParams.put("fileType", "JAR");
		IServerManager serverManager = ServerManagerFactory.getServerManager();
		try {
			versionJARByteArray = serverManager.downloadFromServer(
					Server.MINECRAFT_DOWNLOAD_SERVER, downloadParams);
		} catch (Exception e) {
			logger.log(Level.WARNING,
					"Exception caught while loading version JAR.", e);
			return;
		}

		File file = new File(DirectoryHelper.versions,
				String.format("%s/%s.jar", version.getVersionName(),
						version.getVersionName()));
		OutputStream fos = FileHelper.newOutputStream(file);
		try {
			fos.write(versionJARByteArray);
			fos.close();
		} catch (IOException e) {
			logger.log(Level.WARNING,
					"IOException while writing version JAR data to file.", e);
		}
	}

	public Object[] getVersionJARDownloadEntry(Version version) {
		if (version == null || version.getVersionType() != VersionType.NEW)
			return null;

		Map<String, String> downloadParams = new HashMap<String, String>();
		downloadParams.put("version", version.getVersionName());
		downloadParams.put("fileType", "JAR");
		IServerManager serverManager = ServerManagerFactory.getServerManager();
		String url = serverManager.getDownloadURL(
				Server.MINECRAFT_DOWNLOAD_SERVER, downloadParams);
		OutputStream os = new LazyFileOutputStream(new File(
				DirectoryHelper.versions, String.format("%s/%s.jar",
						version.getVersionName(), version.getVersionName())));

		return new Object[] { url, os };
	}
}
