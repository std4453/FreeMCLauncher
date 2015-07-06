package com.std4453.freemclauncher.assets;

import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.json.JSONObject;

import com.std4453.freemclauncher.files.DirectoryHelper;
import com.std4453.freemclauncher.files.FileHelper;
import com.std4453.freemclauncher.gui.DownloaderMultiple;
import com.std4453.freemclauncher.net.IServerManager;
import com.std4453.freemclauncher.net.Server;
import com.std4453.freemclauncher.net.ServerManagerFactory;
import com.std4453.freemclauncher.util.LazyFileOutputStream;
import com.std4453.freemclauncher.util.StructuredDataHelper;
import com.std4453.freemclauncher.util.StructuredDataObject;

public class AssetsDownloader {
	private static final Logger logger = Logger
			.getLogger("com.std4453.freemclauncher.log.Logger");

	public AssetsDownloader() {
	}

	public void downloadAssets(String name) {
		File file = new File(DirectoryHelper.assets, "indexes/" + name
				+ ".json");
		if (!file.exists())
			return;

		String jsonContent = FileHelper.getFileContentAsString(file);
		JSONObject jsonObject = new JSONObject(jsonContent);
		StructuredDataObject jsonSDO = StructuredDataHelper.fromJSONObject(
				jsonObject).toStructuredDataObject();

		StructuredDataObject objects = jsonSDO
				.getStructuredDataObject("objects");
		boolean virtual = jsonSDO.hasChild("virtual") ? jsonSDO
				.getBoolean("virtual") : false;

		IServerManager manager = ServerManagerFactory.getServerManager();
		Map<String, String> mapping = new HashMap<String, String>();

		Set<String> keys = objects.keySet();
		byte[] data;
		String hash;
		String fileNameExt;
		File assetFile;
		for (String key : keys) {
			hash = objects.getStructuredDataObject(key).getString("hash");
			if (virtual)
				fileNameExt = "virtual/" + name + "/" + key;
			else
				fileNameExt = "objects/" + hash.substring(0, 2) + "/" + hash;
			assetFile = new File(DirectoryHelper.assets, fileNameExt);
			if (assetFile.exists()
					&& assetFile.length() == objects.getStructuredDataObject(
							key).getLong("size"))
				continue;
			mapping.put("assetHash", hash);

			try {
				data = manager
						.downloadFromServer(Server.ASSETS_SERVER, mapping);
			} catch (Exception e) {
				logger.log(Level.WARNING,
						"Exception caught while loading asset.", e);
				return;
			}

			FileHelper.writeToFile(assetFile, data);
		}
	}

	public void downloadAssetsGUI(String name) {
		File file = new File(DirectoryHelper.assets, "indexes/" + name
				+ ".json");
		if (!file.exists())
			return;

		String jsonContent = FileHelper.getFileContentAsString(file);
		JSONObject jsonObject = new JSONObject(jsonContent);
		StructuredDataObject jsonSDO = StructuredDataHelper.fromJSONObject(
				jsonObject).toStructuredDataObject();

		StructuredDataObject objects = jsonSDO
				.getStructuredDataObject("objects");
		boolean virtual = jsonSDO.hasChild("virtual") ? jsonSDO
				.getBoolean("virtual") : false;

		IServerManager manager = ServerManagerFactory.getServerManager();
		Map<String, String> data = new HashMap<String, String>();
		Map<String, OutputStream> mapping = new HashMap<String, OutputStream>();

		String hash, url;
		String fileNameExt;
		File assetFile;
		Set<String> keys = objects.keySet();
		for (String key : keys) {
			hash = objects.getStructuredDataObject(key).getString("hash");
			if (virtual)
				fileNameExt = "virtual/" + name + "/" + key;
			else
				fileNameExt = "objects/" + hash.substring(0, 2) + "/" + hash;
			assetFile = new File(DirectoryHelper.assets, fileNameExt);
			if (assetFile.exists()
					&& assetFile.length() == objects.getStructuredDataObject(
							key).getLong("size"))
				continue;
			data.put("assetHash", hash);

			url = manager.getDownloadURL(Server.ASSETS_SERVER, data);
			mapping.put(fileNameExt + "|" + url, new LazyFileOutputStream(
					assetFile));
		}

		DownloaderMultiple downloader = new DownloaderMultiple(mapping);
		downloader.getFrame().setVisible(true);
		downloader.run();
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			throw new RuntimeException(e);
		}
		
		new AssetsDownloader().downloadAssetsGUI("14w31a");
	}
}
