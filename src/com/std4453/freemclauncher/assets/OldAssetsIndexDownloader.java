package com.std4453.freemclauncher.assets;

import java.io.File;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.std4453.freemclauncher.files.DirectoryHelper;
import com.std4453.freemclauncher.files.FileHelper;
import com.std4453.freemclauncher.net.IServerManager;
import com.std4453.freemclauncher.net.Server;
import com.std4453.freemclauncher.net.ServerManagerFactory;
import com.std4453.freemclauncher.util.LazyFileOutputStream;

public class OldAssetsIndexDownloader {
	private static final Logger logger = LogManager.getLogManager().getLogger(
			"com.std4453.freemclauncher.log.Logger");

	public OldAssetsIndexDownloader() {
	}

	public void downloadOldAssetsIndex() {
		IServerManager manager = ServerManagerFactory.getServerManager();
		try {
			byte[] data = manager.downloadFromServer(
					Server.OLD_ASSETS_INDEX_SERVER, null);
			File file = new File(DirectoryHelper.resources, "resources.xml");
			FileHelper.writeToFile(file, data);
		} catch (Exception e) {
			logger.log(Level.WARNING,
					"Exception caught while downloading old assets index", e);
		}
	}

	public Object[] getOldAssetsIndexDownloadEntry() {
		IServerManager manager = ServerManagerFactory.getServerManager();
		String url = manager.getDownloadURL(Server.OLD_ASSETS_INDEX_SERVER,
				null);
		File file = new File(DirectoryHelper.resources, "resources.xml");
		OutputStream os = new LazyFileOutputStream(file);

		return new Object[] { url, os };
	}
}
