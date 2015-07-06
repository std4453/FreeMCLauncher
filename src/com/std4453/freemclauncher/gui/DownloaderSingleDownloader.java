package com.std4453.freemclauncher.gui;

import com.std4453.freemclauncher.net.Downloader;
import com.std4453.freemclauncher.util.DataSizeFormatHelper;

public class DownloaderSingleDownloader extends Downloader {
	protected DownloaderSingle window;
	protected boolean cancelRequested;
	protected boolean pauseRequested;
	protected long length;
	protected String url;

	public DownloaderSingleDownloader(DownloaderSingle window) {
		super(window.getUrl(), window.getOs());

		this.window = window;
	}

	public void requestCancel() {
		window.getLblNewLabel().setText("请求取消：" + url);
		this.cancelRequested = true;
	}

	public void togglePause() {
		this.pauseRequested = !pauseRequested;
		if (pauseRequested) {
			window.getLblNewLabel().setText("已暂停：" + url);
			window.getBtnNewButton_1().setText("继续");
		} else {
			window.getLblNewLabel().setText("正在下载：" + url);
			window.getBtnNewButton_1().setText("暂停");
		}
	}

	@Override
	protected boolean onDownloadStart(String url, long length) {
		window.getLblNewLabel().setText("正在下载：" + url);
		window.getLblNewLabel_1().setText("0B/s");
		window.getLblNewLabel_2().setText(
				"已下载 0K，共"
						+ DataSizeFormatHelper.formatDataSizeDouble(
								(double) length, -1));
		this.length = length;
		this.url = url;
		
		window.getFrame().setVisible(true);
		return true;
	}

	@Override
	protected void onDownloadProgressChanged(int percent, long downloaded) {
		window.getProgressBar().setValue(percent);
		window.getLblNewLabel_2().setText(
				"已下载 "
						+ DataSizeFormatHelper.formatDataSizeDouble(downloaded,
								-1) + "，共"
						+ DataSizeFormatHelper.formatDataSizeDouble(length, -1));
	}

	@Override
	protected void onDownloadFinished() {
		window.getLblNewLabel().setText("已完成：" + url);
		window.getProgressBar().setValue(window.getProgressBar().getMaximum());
		window.getBtnNewButton().setEnabled(false);
		window.getBtnNewButton_1().setEnabled(false);
	}

	@Override
	protected boolean isCancelled() {
		if (cancelRequested) {
			window.getLblNewLabel().setText("已取消：" + url);
			window.getBtnNewButton_1().setEnabled(false);
			window.getBtnNewButton().setEnabled(false);
		}
		return cancelRequested;
	}

	@Override
	protected boolean isPaused() {
		return pauseRequested;
	}

	@Override
	protected void onDownloadSpeedChanged(float speed) {
		window.getLblNewLabel_1().setText(
				DataSizeFormatHelper.formatDataSizeDouble(speed, -1) + "/s");
	}
}
