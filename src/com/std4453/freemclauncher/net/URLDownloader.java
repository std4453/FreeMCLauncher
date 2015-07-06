package com.std4453.freemclauncher.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;

import com.std4453.freemclauncher.util.DataSizeFormatHelper;

public class URLDownloader {
	public static final int BUFFER_SIZE = 1024;

	public interface OnDownloadProgressChangedListener {
		public void onDownloadProgressChanged(int percent, long amount);
	}

	public interface OnDownloadSpeedChangedListener {
		public void onDownloadSpeedChanged(float bytesPerSecond);
	}

	public static void download(String url,
			OnDownloadProgressChangedListener downloadProgressChangedListener,
			OnDownloadSpeedChangedListener downloadSpeedChangedListener,
			OutputStream dst) throws IOException, URLDownloadException {
		URL urlObject = new URL(url);
		download(urlObject.openConnection(), downloadProgressChangedListener,
				downloadSpeedChangedListener, dst);
	}

	public static void download(URLConnection connection,
			OnDownloadProgressChangedListener downloadProgressChangedListener,
			OnDownloadSpeedChangedListener downloadSpeedChangedListener,
			OutputStream dst) throws IOException, URLDownloadException {
		connection.connect();
		long size = connection.getContentLengthLong();
		if (size < 0)
			throw new URLDownloadException(
					"The length of the requested resource is unknown!");
		InputStream is = connection.getInputStream();
		long downloaded = 0;
		int available;
		byte[] buffer = new byte[BUFFER_SIZE];
		int real;
		int lastPercent = 0;
		int percent;

		long lastTime = System.currentTimeMillis();
		long lastDownload = 0;
		long dt;

		while (downloaded < size) {
			available = is.available();

			do {
				real = is.read(buffer, 0, available > BUFFER_SIZE ? BUFFER_SIZE
						: available);
				downloaded += real;
				available -= real;

				dst.write(buffer, 0, real);

				percent = (int) (100d * downloaded / size);
				if (lastPercent != percent) {
					if (downloadProgressChangedListener != null)
						downloadProgressChangedListener
								.onDownloadProgressChanged(percent, downloaded);
					lastPercent = percent;
				}

				lastDownload += real;
				dt = System.currentTimeMillis() - lastTime;
				if (dt >= 1000) {
					if (downloadSpeedChangedListener != null)
						downloadSpeedChangedListener
								.onDownloadSpeedChanged(lastDownload * 1000f
										/ dt);
					lastDownload = 0;
					lastTime += dt;
				}
			} while (available > 0);
		}
	}

	public static OnDownloadProgressChangedListener getProgressListenerByOutputStream(
			final OutputStream os) {
		final PrintStream ps = new PrintStream(os);

		return new OnDownloadProgressChangedListener() {
			@Override
			public void onDownloadProgressChanged(int percent, long amount) {
				ps.println("Downloaded " + percent + "%, "
						+ DataSizeFormatHelper.formatDataSizeLong(amount)
						+ " in total");
			}
		};
	}

	public static OnDownloadSpeedChangedListener getSpeedListenerByOutputStream(
			final OutputStream os) {
		final PrintStream ps = new PrintStream(os);

		return new OnDownloadSpeedChangedListener() {
			@Override
			public void onDownloadSpeedChanged(float bytesPerSecond) {
				ps.println("Current speed is "
						+ DataSizeFormatHelper.formatDataSizeDouble(
								bytesPerSecond, 0) + "/s");
			}
		};
	}
}
