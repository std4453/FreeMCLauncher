package com.std4453.freemclauncher.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileHelper {
	private static final Logger logger = Logger
			.getLogger("com.std4453.freemclauncher.log.Logger");

	public static String getFileContentAsString(File file) {
		if (file == null || !file.exists() || !file.canRead())
			return null;
		try {
			FileInputStream fis = new FileInputStream(file);

			int available = fis.available();
			byte[] data = new byte[available];
			fis.read(data);
			fis.close();

			return new String(data);
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			logger.log(
					Level.WARNING,
					"IOException caught while reading file \""
							+ file.getAbsolutePath() + file.getName() + "\"", e);
		}

		return null;
	}

	public static String getInternalFileContentAsString(String fileName) {
		if (fileName == null)
			return null;
		try {
			InputStream is = FileHelper.class.getResourceAsStream(fileName);
			int available = is.available();
			byte[] data = new byte[available];
			is.read(data);
			is.close();
			return new String(data);
		} catch (IOException e) {
			logger.log(Level.WARNING,
					"IOException caught while reading internal file\""
							+ fileName + "\"", e);
		}

		return null;
	}

	public static String getAbsoluteFileName(File file) {
		if (file == null)
			return null;
		return file.getAbsolutePath();
	}

	public static void makeSureFilePathExists(File file) {
		if (file == null)
			return;
		if (file.isDirectory())
			file.mkdirs();
		else if (file.getParentFile() != null)
			file.getParentFile().mkdirs();
	}

	public static void makeSureFileExists(File file) {
		if (file == null)
			return;
		if (file.exists())
			return;
		if (file.isDirectory())
			file.mkdirs();
		else {
			file.getParentFile().mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				logger.log(Level.WARNING,
						"IOException caught when creating new File \""
								+ getAbsoluteFileName(file) + "\"", e);
			}
		}
	}

	public static OutputStream newOutputStream(File file) {
		makeSureFilePathExists(file);
		try {
			return new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			logger.log(Level.WARNING,
					"IOException caught when building FileOutputStream for file \""
							+ getAbsoluteFileName(file) + "\"", e);
			return null;
		}
	}

	public static void writeToFile(File file, String data) {
		makeSureFilePathExists(file);
		OutputStream os = newOutputStream(file);
		try {
			os.write(data.getBytes());
			os.close();
		} catch (IOException e) {
			logger.log(Level.WARNING,
					"IOException caught when writing to file \""
							+ getAbsoluteFileName(file) + "\"", e);
		}
	}

	public static void writeToFile(File file, byte[] data) {
		makeSureFilePathExists(file);
		OutputStream os = newOutputStream(file);
		try {
			os.write(data);
			os.close();
		} catch (IOException e) {
			logger.log(Level.WARNING,
					"IOException caught when writing to file \""
							+ getAbsoluteFileName(file) + "\"", e);
		}
	}

	public static boolean delete(File file) {
		if (file == null)
			return true;

		boolean succeeded = true;
		if (file.isDirectory())
			for (File file1 : file.listFiles())
				succeeded &= delete(file1);

		succeeded &= file.delete();

		return succeeded;
	}

	public static void deleteOnExit(final File file) {
		if (file == null)
			return;

		if (file.isFile())
			file.deleteOnExit();
		else
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				@Override
				public void run() {
					while (!FileHelper.delete(file))
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
				}
			}));
	}

	public static String toString(InputStream is) {
		StringBuilder sb = new StringBuilder();
		Scanner scanner = new Scanner(is);
		while (scanner.hasNextLine()) {
			sb.append(scanner.nextLine());
			sb.append('\n');
		}
		scanner.close();

		return sb.toString();
	}
}
