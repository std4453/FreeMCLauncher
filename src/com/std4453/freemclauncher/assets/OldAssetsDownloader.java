package com.std4453.freemclauncher.assets;

import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.apache.tools.ant.util.LazyFileOutputStream;
import org.w3c.dom.Document;

import com.std4453.freemclauncher.files.DirectoryHelper;
import com.std4453.freemclauncher.files.FileHelper;
import com.std4453.freemclauncher.gui.DownloaderMultiple;
import com.std4453.freemclauncher.net.IServerManager;
import com.std4453.freemclauncher.net.Server;
import com.std4453.freemclauncher.net.ServerManagerFactory;
import com.std4453.freemclauncher.util.StructuredDataArray;
import com.std4453.freemclauncher.util.StructuredDataHelper;
import com.std4453.freemclauncher.util.StructuredDataObject;
import com.std4453.freemclauncher.util.XMLHelper;

public class OldAssetsDownloader {
	private static final Logger logger = LogManager.getLogManager().getLogger(
			"com.std4453.freemclauncher.log.Logger");

	public OldAssetsDownloader() {
	}

	/**
	 * In this method, only that whether the resource file exists was checked,
	 * if not, then the resource file is downloaded. However the 'LastModified'
	 * tag should be checked to get updates of the resource if needed, but it
	 * wasn't checked here, because I don't think anybody would make any further
	 * changes to resources of versions lower than 1.6.
	 */
	public void downloadAssets() {
		File file = new File(DirectoryHelper.resources, "resources.xml");
		String content = FileHelper.getFileContentAsString(file);
		Document doc = XMLHelper.getDocument(content);
		StructuredDataObject sdo = StructuredDataHelper.fromXMLDocument(doc)
				.toStructuredDataObject();

		StructuredDataArray contents = sdo.getStructuredDataArray("Contents");

		Map<String, String> data = new HashMap<String, String>();

		IServerManager manager = ServerManagerFactory.getServerManager();

		for (Object object : contents) {
			if (object instanceof StructuredDataObject) {
				StructuredDataObject contentSDO = (StructuredDataObject) object;
				String key = contentSDO.getStructuredDataArray("Key")
						.getStructuredDataObject(0).getString("#text");
				int size = contentSDO.getStructuredDataArray("Size")
						.getStructuredDataObject(0).getInt("#text");

				if (key.endsWith("/"))
					continue;

				data.put("assetPath", key);
				File dst = new File(DirectoryHelper.resources, key);
				if (dst.exists() && dst.length() == size)
					continue;

				try {
					byte[] assetData = manager.downloadFromServer(
							Server.OLD_ASSETS_SERVER, data);

					FileHelper.writeToFile(dst, assetData);
				} catch (Exception e) {
					logger.log(Level.WARNING,
							"Exception caught while download asset \"" + key
									+ "\"", e);
				}
			}
		}
	}

	public void downloadAssetsGUI() {
		File file = new File(DirectoryHelper.resources, "resources.xml");
		String content = FileHelper.getFileContentAsString(file);
		Document doc = XMLHelper.getDocument(content);
		StructuredDataObject sdo = StructuredDataHelper.fromXMLDocument(doc)
				.toStructuredDataObject();

		StructuredDataArray contents = sdo
				.getStructuredDataArray("ListBucketResult")
				.getStructuredDataObject(0).getStructuredDataArray("Contents");

		Map<String, OutputStream> mapping = new HashMap<String, OutputStream>();
		Map<String, String> data = new HashMap<String, String>();

		IServerManager manager = ServerManagerFactory.getServerManager();

		for (Object object : contents) {
			if (object instanceof StructuredDataObject) {
				StructuredDataObject contentSDO = (StructuredDataObject) object;
				String key = contentSDO.getStructuredDataArray("Key")
						.getStructuredDataObject(0).getString("#text");
				int size = contentSDO.getStructuredDataArray("Size")
						.getStructuredDataObject(0).getInt("#text");

				if (key.endsWith("/"))
					continue;

				data.put("assetPath", key);
				File dst = new File(DirectoryHelper.resources, key);
				if (dst.exists() && dst.length() == size)
					continue;

				String url = manager.getDownloadURL(Server.OLD_ASSETS_SERVER,
						data);
				OutputStream os = new LazyFileOutputStream(dst);

				mapping.put(url, os);
			}
		}

		DownloaderMultiple downloader = new DownloaderMultiple(mapping);
		downloader.getFrame().setVisible(true);
		downloader.run();
		downloader.getFrame().dispose();
	}
}
