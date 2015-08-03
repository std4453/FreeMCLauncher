package com.std4453.freemclauncher.tests;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.UIManager;

import com.std4453.freemclauncher.gui.DownloaderMultiple;

public class DownloadGradle {
	public static void main(String[] args) throws Throwable {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		Map<String, OutputStream> mapping = new HashMap<>();
		mapping.put(
				"http://services.gradle.org/distributions/gradle-2.0-bin.zip",
				new FileOutputStream(
						new File("D:\\MCForge\\gradle-2.0-bin.zip")));
		
		DownloaderMultiple downloader=new DownloaderMultiple(mapping);
		downloader.getFrame().setVisible(true);
		downloader.run();
	}
}
