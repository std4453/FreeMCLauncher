package com.std4453.freemclauncher.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Downloader {
	public static final int BUFFER_SIZE = 1024;

	protected String url;
	protected OutputStream dst;

	public Downloader(String url, OutputStream dst) {
		this.url = url;
		this.dst = dst;
	}

	public void run() throws IOException {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet get = new HttpGet(url);
		int times = 0;
		long size;
		InputStream is;
		HttpEntity entity;
		CloseableHttpResponse response;
		do {
			response = client.execute(get);
			entity = response.getEntity();
			is = entity.getContent();
			size = entity.getContentLength();
			++times;
		} while (size <= 0 && times < 10);
		if (size <= 0)
			return;
		if (!onDownloadStart(url, size)) {
			is.close();
			response.close();
			client.close();
			return;
		}

		long downloaded = 0;
		int available;
		byte[] buffer = new byte[BUFFER_SIZE];
		int real;
		int percent;

		long lastTime = System.currentTimeMillis();
		long lastDownload = 0;
		long dt;

		while (downloaded < size) {
			available = is.available();

			do {
				if (isPaused()) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}

				if (isCancelled()) {
					is.close();
					response.close();
					client.close();
					return;
				}

				real = is.read(buffer, 0, available > BUFFER_SIZE ? BUFFER_SIZE
						: available);
				downloaded += real;
				available -= real;

				dst.write(buffer, 0, real);

				percent = (int) (100d * downloaded / size);
				onDownloadProgressChanged(percent, downloaded);

				lastDownload += real;
				dt = System.currentTimeMillis() - lastTime;
				if (dt >= 1000) {
					onDownloadSpeedChanged(lastDownload * 1000f / dt);
					lastDownload = 0;
					lastTime += dt;
				}
			} while (available > 0);
		}

		onDownloadFinished();

		is.close();
		response.close();
		client.close();
	}

	protected boolean isCancelled() {
		return false;
	}

	protected boolean isPaused() {
		return false;
	}

	protected boolean onDownloadStart(String url, long length) {
		return true;
	}

	protected void onDownloadProgressChanged(int percent, long downloaded) {
	}

	protected void onDownloadSpeedChanged(float speed) {
		return;
	}

	protected void onDownloadFinished() {
	}
}
