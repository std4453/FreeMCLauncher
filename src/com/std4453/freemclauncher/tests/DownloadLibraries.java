package com.std4453.freemclauncher.tests;

import java.io.File;
import java.io.OutputStream;
import java.util.Map;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.std4453.freemclauncher.files.DirectoryHelper;
import com.std4453.freemclauncher.gui.DownloaderMultiple;
import com.std4453.freemclauncher.libraries.LibraryDownloader;
import com.std4453.freemclauncher.versions.Version;
import com.std4453.freemclauncher.versions.VersionIndexer;

public class DownloadLibraries {
	/**
	 * download all libraried for versions.<br>
	 * (the versions list is at /.minecraft/versions/versions.json, though it is
	 * the same with
	 * http://s3.amazonaws.com/Minecraft.Download/versions/versions.json)
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			throw new RuntimeException(e);
		}

		VersionIndexer indexer = new VersionIndexer();
		indexer.loadVersionIndexFromFile(new File(DirectoryHelper.versions,
				"versions.json"));
		LibraryDownloader libraryDownloader = new LibraryDownloader();
		for (Version version : indexer.getVersions()) {
			Map<String, OutputStream> mapping = libraryDownloader
					.getLibrariesDownloadMapping(version);
			if (mapping.size() == 0)
				continue;
			DownloaderMultiple downloader = new DownloaderMultiple(mapping);
			downloader.getFrame().setVisible(true);
			downloader.run();
			Thread.sleep(1000);
			downloader.getFrame().dispose();
		}
	}
}
