package com.std4453.freemclauncher.net;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.std4453.freemclauncher.files.DirectoryHelper;
import com.std4453.freemclauncher.files.FileHelper;

public class DownloaderAutoRetry {
	private static final Logger logger = Logger
			.getLogger("com.std4453.freemclauncher.log.Logger");

	protected CloseableHttpClient client;

	protected String url;
	protected OutputStream os;
	protected long length;
	protected boolean rangeAllowed;
	protected boolean available;

	protected HttpGet get;
	protected long downloaded;

	protected long lastRead;

	protected long lastTime;
	protected long lastDownload;
	protected int percent;

	public DownloaderAutoRetry(String url, OutputStream os) {
		this.url = url;
		this.os = os;
	}

	public void run() {
		client = HttpClients.createDefault();
		int retries = 0;
		do {
			++retries;
			logger.log(Level.FINER, "Availability check try #" + retries);
			checkAvailabilityAndGetLength();
		} while (!available && retries < 10);

		System.out.println("Size=" + length);

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (downloaded < length) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					if (lastRead == 0)
						continue;

					long time = System.currentTimeMillis();
					if (time - lastRead > 20000) {
						get.abort();
						logger.log(Level.WARNING, "Download reset at position "
								+ downloaded);
						lastRead = time;
					}
				}
			}
		}).start();

		onDownloadStart(url, length);

		while (downloaded < length) {
			lastRead = System.currentTimeMillis();
			download(rangeAllowed ? downloaded : 0L);
		}

		onDownloadFinished();
	}

	protected void download(long from) {
		InputStream is = null;
		CloseableHttpResponse response = null;
		try {
			get = new HttpGet(url);
			get.addHeader("Range", "bytes=" + from + "-");
			response = client.execute(get);
			is = response.getEntity().getContent();

			// download
			downloaded = from;
			int real;
			long time;

			final int BUFFER_SIZE = 4096;
			byte[] buffer = new byte[BUFFER_SIZE];

			while (downloaded < length) {
				real = is.read(buffer);

				time = System.currentTimeMillis();

				lastRead = time;
				os.write(buffer, 0, real);
				downloaded += real;

				percent = (int) (downloaded * 100d / length);
				onDownloadProgressChanged(percent, downloaded);

				lastDownload += real;
				if (time - lastTime > 1000) {
					onDownloadSpeedChanged(lastDownload * 1000f
							/ (time - lastTime));
					lastDownload = 0;
					lastTime = time;
				}

				System.out.println(downloaded);
			}
		} catch (IOException e) {
			if ((e instanceof SocketException)
					&& (e.getMessage().trim().equals("Socket closed")))
				return;
			logger.log(Level.WARNING, "IOException caught while downloading.",
					e);
		} finally {
			try {
				if (is != null)
					is.close();
				if (response != null)
					response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected void onDownloadStart(String url, long length) {
	}

	protected void onDownloadProgressChanged(int percent, long bytes) {
	}

	protected void onDownloadSpeedChanged(float speed) {
	}

	protected void onDownloadFinished() {
	}

	protected void checkAvailabilityAndGetLength() {
		try {
			// make sure resource is available and get its length
			HttpHead head = new HttpHead(url);
			CloseableHttpResponse response = client.execute(head);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				available = false;
				response.close();
				return;
			}
			Header[] contentSizeHeaders = response.getHeaders("Content-Length");
			if (contentSizeHeaders.length > 0) {
				String contentSizeString = contentSizeHeaders[0].getValue();
				this.length = Long.parseLong(contentSizeString);
			} else {
				available = false;
				response.close();
				return;
			}
			available = true;
			rangeAllowed = true;
			Header[] rangesHeaders = response.getHeaders("Accept-Ranges");
			if (rangesHeaders.length > 0
					&& rangesHeaders[0].getValue().equals("none")) {
				rangeAllowed = false;
				response.close();
				return;
			}
			if (rangesHeaders.length > 0
					&& rangesHeaders[0].getValue().equals("bytes")) {
				rangeAllowed = true;
				response.close();
				return;
			}
			response.close();
			// re-check range availability
			head = new HttpHead(url);
			head.addHeader("Range", "bytes=0-" + length);
			response = client.execute(head);
			statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 206)
				rangeAllowed = true;
			else
				rangeAllowed = false;
			response.close();
		} catch (IOException e) {
			logger.log(Level.WARNING,
					"IOException caught while checking availability", e);
			available = false;
			return;
		}
	}

	public static void main(String[] args) {
		String versionName = "1.5.2";
		Map<String, String> data = new HashMap<String, String>();
		data.put("version", versionName);
		data.put("fileType", "JAR");
		File file = new File(DirectoryHelper.versions, versionName + "/"
				+ versionName + ".jar");
		OutputStream os = FileHelper.newOutputStream(file);
		IServerManager manager = ServerManagerFactory.getServerManager();
		String url = manager.getDownloadURL(Server.MINECRAFT_DOWNLOAD_SERVER,
				data);
		DownloaderAutoRetry downloader = new DownloaderAutoRetry(url, os);
		downloader.run();
	}
}
