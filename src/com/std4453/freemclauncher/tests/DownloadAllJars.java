package com.std4453.freemclauncher.tests;

import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.std4453.freemclauncher.files.DirectoryHelper;
import com.std4453.freemclauncher.gui.DownloaderMultiple;
import com.std4453.freemclauncher.net.IServerManager;
import com.std4453.freemclauncher.net.Server;
import com.std4453.freemclauncher.net.ServerManagerFactory;
import com.std4453.freemclauncher.util.LazyFileOutputStream;
import com.std4453.freemclauncher.versions.Version;
import com.std4453.freemclauncher.versions.VersionIndexer;

public class DownloadAllJars {
	/**
	 * download all the minecraft JARs
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			throw new RuntimeException(e);
		}

		IServerManager manager = ServerManagerFactory.getServerManager();
		VersionIndexer indexer = new VersionIndexer();
		indexer.loadVersionIndex();
		List<Version> versions = indexer.getVersions();
		Map<String, OutputStream> mapping = new HashMap<String, OutputStream>();
		Map<String, String> data = new HashMap<String, String>();
		String name;
		String url;
		File file;
		for (Version version : versions) {
			name = version.getVersionName();
			data.put("fileType", "JAR");
			data.put("version", name);
			url = manager
					.getDownloadURL(Server.MINECRAFT_DOWNLOAD_SERVER, data);
			file = new File(DirectoryHelper.versions, name + "/" + name
					+ ".jar");
			if (file.exists())
				continue;
			mapping.put(url, new LazyFileOutputStream(file));
		}

		DownloaderMultiple window = new DownloaderMultiple(mapping);
		window.getFrame().setVisible(true);
		window.run();
	}
}
