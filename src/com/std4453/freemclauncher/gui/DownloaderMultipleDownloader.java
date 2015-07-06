package com.std4453.freemclauncher.gui;

import java.io.IOException;

import com.std4453.freemclauncher.net.DownloaderAutoRetry;
import com.std4453.freemclauncher.util.DataSizeFormatHelper;

public class DownloaderMultipleDownloader extends DownloaderAutoRetry {
	protected DownloaderMultiple window;
	protected long length;
	protected String url;

	protected long bytes;
	protected float speed;
	protected int percent;

	public DownloaderMultipleDownloader(DownloaderMultiple window) {
		super(window.getCurrentUrl(), window.getCurrentOs());

		this.window = window;
	}

	@Override
	protected void onDownloadStart(String url, long length) {
		this.length = length;
		this.url = url;
		
		window.getModel().add("开始下载"+url);
		window.getProgressBar().setValue(0);
		
		setText();
	}

	@Override
	protected void onDownloadProgressChanged(int percent, long downloaded) {
		this.percent = percent;
		this.bytes = downloaded;
		
		setText();

		window.getProgressBar().setValue(percent);
	}

	@Override
	protected void onDownloadFinished() {
		window.getProgressBar().setValue(window.getProgressBar().getMaximum());
		window.getModel().add("下载完成");
		
		try {
			if (os!=null)
				os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDownloadSpeedChanged(float speed) {
		this.speed = speed;
		
		setText();
	}

	private void setText() {
		window.getLblNewLabel().setText(
				DataSizeFormatHelper.formatDataSizeDouble(speed, -1) + "/s ("
						+ percent + "% of "
						+ DataSizeFormatHelper.formatDataSizeDouble(length, -1)
						+ ")");
	}
}
