package com.std4453.freemclauncher.assets;

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
import com.std4453.freemclauncher.util.StructuredDataObject;

public class AssetsIndexDownloader {
	private static final Logger logger = Logger
			.getLogger("com.std4453.freemclauncher.log.Logger");

	public AssetsIndexDownloader() {
	}

	public String getAssetsIndexNameFromVersionJSON(StructuredDataObject json) {
		if (json.hasChild("assets"))
			return json.getString("assets");
		else
			return "legacy";
	}

	public void downAssetsIndex(String indexName) {
		IServerManager manager = ServerManagerFactory.getServerManager();
		Map<String, String> data = new HashMap<String, String>();
		data.put("indexName", indexName);

		byte[] assetsIndexByteArray;

		try {
			assetsIndexByteArray = manager.downloadFromServer(
					Server.ASSETS_INDEX_SERVER, data);
		} catch (Exception e) {
			logger.log(Level.WARNING,
					"Exception caught while downloading assets index \""
							+ indexName + "\"");
			return;
		}

		File file = new File(DirectoryHelper.assets, "indexes/" + indexName
				+ ".json");
		OutputStream os = FileHelper.newOutputStream(file);

		try {
			os.write(assetsIndexByteArray);
			os.close();
		} catch (IOException e) {
			logger.log(Level.WARNING,
					"IOException caught while writing assets index file.", e);
		}
	}

	public Object[] getAssetsIndexDownloadEntry(String indexName) {
		IServerManager manager = ServerManagerFactory.getServerManager();
		Map<String, String> data = new HashMap<String, String>();
		data.put("indexName", indexName);

		String url = manager.getDownloadURL(Server.ASSETS_INDEX_SERVER, data);

		File file = new File(DirectoryHelper.assets, "indexes/" + indexName
				+ ".json");
		OutputStream os = new LazyFileOutputStream(file);

		return new Object[] { url, os };
	}
}
