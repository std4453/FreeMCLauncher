package com.std4453.freemclauncher.tests;

import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.json.JSONObject;

import com.std4453.freemclauncher.assets.AssetsIndexDownloader;
import com.std4453.freemclauncher.files.DirectoryHelper;
import com.std4453.freemclauncher.files.FileHelper;
import com.std4453.freemclauncher.gui.DownloaderMultiple;
import com.std4453.freemclauncher.util.StructuredDataHelper;
import com.std4453.freemclauncher.util.StructuredDataObject;
import com.std4453.freemclauncher.versions.Version;
import com.std4453.freemclauncher.versions.VersionIndexer;

public class DownloadAllAssetIndexes {
	/**
	 * download all asset index files linked by version info files.
	 * (/.minecraft/versions/<VERSION_NAME>/<VERSION_NAME>.json)
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			throw new RuntimeException(e);
		}
		
		VersionIndexer indexer = new VersionIndexer();
		indexer.loadVersionIndexFromFile(new File(DirectoryHelper.versions,
				"versions.json"));
		AssetsIndexDownloader assets = new AssetsIndexDownloader();
		Map<String, OutputStream> mapping = new HashMap<String, OutputStream>();
		for (Version version : indexer.getVersions()) {
			StructuredDataObject sdo = getVersionJSON(version);
			String assetsIndexName = assets
					.getAssetsIndexNameFromVersionJSON(sdo);
			Object[] entry = assets
					.getAssetsIndexDownloadEntry(assetsIndexName);
			mapping.put((String) entry[0], (OutputStream) entry[1]);
		}

		DownloaderMultiple downloader = new DownloaderMultiple(mapping);
		downloader.getFrame().setVisible(true);
		downloader.run();
	}

	protected static StructuredDataObject getVersionJSON(Version version) {
		File file = new File(DirectoryHelper.versions, version.getVersionName()
				+ "/" + version.getVersionName() + ".json");
		String jsonData = FileHelper.getFileContentAsString(file);
		JSONObject object = new JSONObject(jsonData);

		return StructuredDataHelper.fromJSONObject(object)
				.toStructuredDataObject();
	}
}
