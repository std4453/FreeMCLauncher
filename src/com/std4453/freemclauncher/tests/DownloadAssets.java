package com.std4453.freemclauncher.tests;

import java.io.File;
import java.io.FilenameFilter;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.std4453.freemclauncher.assets.AssetsDownloader;
import com.std4453.freemclauncher.files.DirectoryHelper;

public class DownloadAssets {
	/**
	 * download all asset files listed in
	 * /.minecraft/assets/indexes/<ASSETS_INDEX_NAME>.json
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

		File file = new File(DirectoryHelper.assets, "indexes/");
		File[] files = file.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".json");
			}
		});
		for (File indexFile : files) {
			String indexName = indexFile.getName();
			indexName = indexName.substring(0, indexName.lastIndexOf('.'));

			AssetsDownloader downloader = new AssetsDownloader();
			downloader.downloadAssetsGUI(indexName);
		}
	}
}
