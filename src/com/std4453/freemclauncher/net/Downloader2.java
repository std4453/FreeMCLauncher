package com.std4453.freemclauncher.net;

import static com.std4453.freemclauncher.logging.Logger.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.std4453.freemclauncher.files.FileHelper;

public class Downloader2 {
	private class SpeedMonitorThread extends Thread {
		protected List<Float> savedSpeeds;

		protected long lastDownloaded;
		protected long lastTime;

		protected long totalTime;
		protected long totalDownloaded;

		public SpeedMonitorThread() {
			this.savedSpeeds = new Vector<Float>();
		}

		@Override
		public void run() {
			boolean downloadStarted = false;

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				log(ERROR, "Speed monitor thread interrupted while sleeping!");
				log(ERROR, e);
				dispatchOnError("download.stage.speedMonitor.initialSleep", e);
			}

			synchronized (Downloader2.this) {
				downloadStarted = Downloader2.this.downloadStarted;
			}

			while (downloadStarted) {
				long lastDownloaded, lastTime;
				long totalDownloaded, totalTime;
				long currentTime = System.currentTimeMillis();

				synchronized (this) {
					lastDownloaded = this.lastDownloaded;
					lastTime = this.lastTime;

					this.totalDownloaded += lastDownloaded;
					this.totalTime += currentTime - lastTime;

					totalDownloaded = this.totalDownloaded;
					totalTime = this.totalTime;

					this.lastDownloaded = 0;
					this.lastTime = currentTime;
				}

				long dt = currentTime - lastTime;
				float bps = lastDownloaded * 1000f / dt;
				if (this.savedSpeeds.size() >= 10)
					this.savedSpeeds.remove(0);
				this.savedSpeeds.add(bps);

				float totSpd = totalDownloaded * 1000f / totalTime;

				dispatchDynamicDownloadSpeed(bps, calcAvg(), totSpd);

				try {
					Thread.sleep(currentTime + 1000
							- System.currentTimeMillis());
				} catch (InterruptedException e) {
					log(ERROR,
							"Speed monitor thread interrupted while sleeping!");
					log(ERROR, e);
					dispatchOnError("download.stage.speedMonitor.initialSleep",
							e);
				}

				synchronized (Downloader2.this) {
					downloadStarted = Downloader2.this.downloadStarted;
				}
			}
		}

		protected synchronized void onPackageReceived(long size) {
			this.lastDownloaded += size;
		}

		protected float calcAvg() {
			float sum = 0;
			for (float spd : this.savedSpeeds)
				sum += spd;

			return sum / this.savedSpeeds.size();
		}
	}

	private class AutoRetryThread extends Thread {
		public AutoRetryThread() {
		}

		@Override
		public void run() {
			dispatchOnAutoRetryThreadStart(this);

			boolean stopRequested = false;
			boolean downloadPaused = false;
			boolean downloadStarted = false;

			synchronized (Downloader2.this) {
				stopRequested = Downloader2.this.stopRequested;
				downloadPaused = Downloader2.this.downloadPaused;
				downloadStarted = Downloader2.this.downloadStarted;
			}

			while (downloadStarted) {
				for (DownloadThread thread : Downloader2.this.downloadThreads)
					if (thread != null)
						if (stopRequested || downloadPaused)
							killThreadIfNeeded(thread);

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					log(ERROR,
							String.format("Auto retry thread interrupted while sleeping!"));
				}

				synchronized (Downloader2.this) {
					stopRequested = Downloader2.this.stopRequested;
					downloadPaused = Downloader2.this.downloadPaused;
					downloadStarted = Downloader2.this.downloadStarted;
				}
			}
		}

		private void killThreadIfNeeded(DownloadThread thread) {
			if (thread == null)
				return;

			if (!thread.isAlive())
				return;

			long timeout = Downloader2.this.conf.getTimeout();
			if (timeout < 0)
				return;

			HttpGet threadGetMethod = thread.getMethod;
			if (threadGetMethod == null)
				return;

			long currentTime = System.currentTimeMillis();
			long threadLastRecv = thread.lastRecv;

			if (threadLastRecv > currentTime + timeout) {
				dispatchOnDownloadThreadForcedRestarted(thread);
				try {
					threadGetMethod.abort();
				} catch (Throwable t) {
					log(ERROR, String.format(
							"Error while stopping download thread \"%s\"",
							thread.getName()));
					log(ERROR, t);
					dispatchOnError("download.stage.stopThread", t);
				}
			}
		}
	}

	private class DownloadThread extends Thread {
		protected long startPos;
		protected long endPos;

		// network resources, should be null while thread stopped
		protected CloseableHttpClient client;
		protected HttpGet getMethod;
		protected CloseableHttpResponse response;

		// cache file
		protected RandomAccessFile cacheFile;

		// thread states
		protected long lastRecv;

		public DownloadThread(long startPos, long endPos) {
			this.startPos = startPos;
			this.endPos = endPos;
		}

		@Override
		public void run() {
			dispatchOnDownloadThreadStart(this);

			try {
				cacheFile = new RandomAccessFile(Downloader2.this.cacheFile,
						"rw");
			} catch (FileNotFoundException e) {
				log(ERROR, "Error while opening cache file!");
				log(ERROR, e);
				dispatchOnError("download.stage.downloadThread.openCacheFile",
						e);
				dispatchOnDownloadThreadStopped(true);
				throw new RuntimeException(e);
			}

			boolean stopRequested = false;
			boolean downloadPaused = false;

			synchronized (Downloader2.this) {
				stopRequested = Downloader2.this.stopRequested;
				downloadPaused = Downloader2.this.downloadPaused;
			}

			while (!stopRequested && startPos < endPos) {
				if (!downloadPaused)
					download();

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					log(ERROR, String.format(
							"Thread \"%s\" interrupted while sleeping!",
							this.getName()));
				}

				synchronized (Downloader2.this) {
					stopRequested = Downloader2.this.stopRequested;
					downloadPaused = Downloader2.this.downloadPaused;
				}

				if (!stopRequested && startPos < endPos) {
					if (!Downloader2.this.rangeEnabled)
						startPos = 0;

					dispatchOnDownloadThreadRestarted(this);
				}
			}

			dispatchOnDownloadThreadStopped(false);
		}

		private void download() {
			try {
				client = HttpClients.createDefault();
				getMethod = new HttpGet(Downloader2.this.conf.getUri());
				getMethod.addHeader("Range",
						String.format("bytes=%d-%d", startPos, endPos));
				response = client.execute(getMethod);
				InputStream is = response.getEntity().getContent();

				int bufferSize = Downloader2.this.conf.getBufferSize();
				byte[] buffer = new byte[bufferSize];

				int real;
				lastRecv = System.currentTimeMillis();

				while (startPos < endPos) {
					real = is.read(buffer);
					lastRecv = System.currentTimeMillis();

					dispatchOnPackageReceived(this, startPos, real, buffer);

					cacheFile.seek(startPos);
					cacheFile.write(buffer, 0, real);
					startPos += real;
				}

				response.close();
				client.close();

				client = null;
				getMethod = null;
				response = null;
			} catch (Throwable t) {
				log(ERROR, "Error caught while downloading!");
				log(ERROR, t);
				dispatchOnError("download.stage.downloadThread.downloading", t);
			}
		}
	}

	public static interface DownloadCallback {
		public void onDownloadStart();

		public void onResourceAvailabilityCheckStart();

		public void onResourceAvailabilityCheckEnd();

		public void onResourceUnavailable();

		public void onDownloadThreadStart(Thread thread);

		public void onDownloadFinished();

		public void onDownloadFailed(String message);

		public void onDownloadSucceed(byte[] data);

		public void onCacheFileCreated(File cacheFile);

		public void onError(String occasion, Throwable t);

		public void onDownloadThreadRestarted(Thread thread);

		public void onPackageReceived(Thread thread, long offset, long size,
				byte[] buffer);

		public void onDownloadThreadStopped(boolean onError);

		public void onAutoRetryThreadStart(Thread autoRetryThread);

		public void onDownloadThreadForcedRestarted(Thread thread);

		public void dynamicDownloadSpeed(float lastSec, float sec10Avg,
				float totAvg);
	}

	public static class DownloadCallbackAdapter implements DownloadCallback {
		@Override
		public void onDownloadStart() {
		}

		@Override
		public void onResourceAvailabilityCheckStart() {
		}

		@Override
		public void onResourceAvailabilityCheckEnd() {
		}

		@Override
		public void onResourceUnavailable() {
		}

		@Override
		public void onDownloadThreadStart(Thread thread) {
		}

		@Override
		public void onDownloadFinished() {
		}

		@Override
		public void onDownloadFailed(String message) {
		}

		@Override
		public void onDownloadSucceed(byte[] data) {
		}

		@Override
		public void onCacheFileCreated(File cacheFile) {
		}

		@Override
		public void onError(String occasion, Throwable t) {
		}

		@Override
		public void onDownloadThreadRestarted(Thread thread) {
		}

		@Override
		public void onPackageReceived(Thread thread, long offset, long size,
				byte[] buffer) {
		}

		@Override
		public void onDownloadThreadStopped(boolean onError) {
		}

		@Override
		public void onAutoRetryThreadStart(Thread autoRetryThread) {
		}

		@Override
		public void onDownloadThreadForcedRestarted(Thread thread) {
		}

		@Override
		public void dynamicDownloadSpeed(float lastSec, float sec10Avg,
				float totAvg) {
		}
	}

	public static final class DownloadConf {
		private static final int DEFAULT_MAX_THREADS = 10;
		private static final int DEFAULT_BUFFER_SIZE = 4096;
		private static final long DEFAULT_TIMEOUT = 20 * 1000;

		public String uri;
		public DownloadCallback callback;
		public boolean async;
		public boolean forceSingleThread;
		public int overrideMaxThreads;
		public int overrideBufferSize;
		public long overrideTimeout;

		public DownloadConf(String uri, DownloadCallback callback,
				boolean async, boolean forceSingleThread,
				int overrideMaxThreads, int overrideBufferSize,
				long overrideTimeout) {
			this.uri = uri;
			this.callback = callback;
			this.async = async;
			this.forceSingleThread = forceSingleThread;
			this.overrideMaxThreads = overrideMaxThreads;
			this.overrideBufferSize = overrideBufferSize;
			this.overrideTimeout = overrideTimeout;
		}

		public DownloadConf(String uri, DownloadCallback callback, boolean async) {
			this(uri, callback, async, false, -1, -1, -1);
		}

		public DownloadConf(String uri, DownloadCallback callback) {
			this(uri, callback, false);
		}

		public DownloadConf(String uri) {
			this(uri, null);
		}

		protected DownloadConf(DownloadConf conf) {
			this.uri = conf.uri;
			this.callback = conf.callback;
			this.async = conf.async;
			this.forceSingleThread = conf.forceSingleThread;
			this.overrideBufferSize = conf.overrideBufferSize;
			this.overrideMaxThreads = conf.overrideMaxThreads;
		}

		public String getUri() {
			return uri;
		}

		public void setUri(String uri) {
			this.uri = uri;
		}

		public DownloadCallback getCallback() {
			return callback;
		}

		public void setCallback(DownloadCallback callback) {
			this.callback = callback;
		}

		public boolean isAsync() {
			return async;
		}

		public void setAsync(boolean async) {
			this.async = async;
		}

		public boolean isForceSingleThread() {
			return forceSingleThread;
		}

		public void setForceSingleThread(boolean forceSingleThread) {
			this.forceSingleThread = forceSingleThread;
		}

		public int getOverrideMaxThreads() {
			return overrideMaxThreads;
		}

		public void setOverrideMaxThreads(int overrideMaxThreads) {
			this.overrideMaxThreads = overrideMaxThreads;
		}

		public int getOverrideBufferSize() {
			return overrideBufferSize;
		}

		public void setOverrideBufferSize(int overrideBufferSize) {
			this.overrideBufferSize = overrideBufferSize;
		}

		public int getBufferSize() {
			return overrideBufferSize > 0 ? overrideBufferSize
					: DEFAULT_BUFFER_SIZE;
		}

		public int getMaxThreads() {
			return forceSingleThread ? 1
					: overrideMaxThreads > 0 ? overrideMaxThreads
							: DEFAULT_MAX_THREADS;
		}

		public long getTimeout() {
			return overrideTimeout > 100 ? overrideTimeout
					: overrideTimeout == -1 ? -1 : DEFAULT_TIMEOUT;
		}
	}

	// each thread not more than 1MB
	public static final long MIN_CHUNK_PER_THREAD = 1 * 1024 * 1024;

	// threads
	protected DownloadThread[] downloadThreads;
	protected SpeedMonitorThread speedMonitorThread;
	protected AutoRetryThread autoRetryThread;

	// current configuration (copied, immutable)
	protected DownloadConf conf;

	// downloader states
	protected boolean downloading;
	protected boolean downloadStarted;
	protected boolean downloadPaused = false;
	protected boolean stopRequested = false;

	// resource info
	protected long resourceSize;
	protected boolean rangeEnabled;
	protected File cacheFile;

	// not null if this downloader is only a proxy of an asynchronous one
	protected Downloader2 asyncProxy;

	public Downloader2() {
		asyncProxy = null;
	}

	// main method

	public byte[] download(final DownloadConf conf) {
		if (this.downloading)
			return null;

		if (conf == null)
			return null;

		if (conf.async) {
			conf.setAsync(false);
			new Thread(new Runnable() {
				@Override
				public void run() {
					downloading = true;
					(asyncProxy = new Downloader2()).download(conf);
					downloading = false;
				}
			}).start();
			return null;
		}

		this.conf = new DownloadConf(conf);

		this.downloadThreads = null;
		this.downloading = true;
		this.downloadPaused = false;
		this.stopRequested = false;

		dispatchOnDownloadStart();

		dispatchOnResourceAvailabilityCheckStart();
		boolean available = false;
		try {
			available = checkResourceAvailability();
		} catch (Throwable t) {
			dispatchOnError("download.stage.checkResourceAvailability", t);
			available = false;
		}
		dispatchOnResourceAvailabilityCheckEnd();
		if (!available) {
			dispatchOnResourceUnavailable();
			this.downloading = false;
			dispatchOnDownloadFinished();
			dispatchOnDownloadFailed("download.fail.resourceUnavailable");
			return null;
		}

		try {
			cacheFile = createDownloadCacheFile();
			dispatchOnCacheFileCreated(cacheFile);
		} catch (Throwable t) {
			cacheFile = null;
			dispatchOnError("download.stage.createDownloadCacheFile", t);
			this.downloading = false;
			dispatchOnDownloadFinished();
			dispatchOnDownloadFailed("download.fail.cannotCreateCacheFile");
			return null;
		}
		try {
			calculateAndStartDownloadThreads();
			downloadStarted = true;
		} catch (Throwable t) {
			downloadStarted = false;
			stopDownloadThreads();
			dispatchOnError("download.stage.startDownloadThreads", t);
			this.downloading = false;
			dispatchOnDownloadFinished();
			dispatchOnDownloadFailed("download.fail.cannotCreateDownloadThreads");
			return null;
		}
		try {
			startDaemonThreads();
		} catch (Throwable t) {
			downloadStarted = false;
			stopDownloadThreads();
			dispatchOnError("download.stage.startDeamonThreads", t);
			this.downloading = false;
			dispatchOnDownloadFinished();
			dispatchOnDownloadFailed("download.fail.cannotCreateDaemonThreads");
			return null;
		}
		while (!downloadThreadsStopped()) {
			try {
				waitUntilThreadsStop();
			} catch (Throwable t) {
				dispatchOnError("download.stage.waitUntilThreadsStop", t);
			}
		}
		downloadStarted = false;
		this.downloadThreads = null;
		waitUntilDaemonThreadsStop();

		if (stopRequested) {
			this.downloading = false;
			dispatchOnDownloadFinished();
			dispatchOnDownloadFailed("download.fail.cancelled");
		}

		byte[] data = null;
		try {
			data = readCacheFileData();
		} catch (Throwable t) {
			dispatchOnError("download.stage.readCacheFileData", t);
			this.downloading = false;
			dispatchOnDownloadFinished();
			dispatchOnDownloadFailed("download.file.cannotReadCacheFile");
			return null;
		}

		this.downloading = false;

		dispatchOnDownloadFinished();
		dispatchOnDownloadSucceed(data);
		return data;
	}

	// login methods

	private boolean checkResourceAvailability() throws IOException,
			NumberFormatException {
		// make sure resource is available and get its length
		CloseableHttpClient client = HttpClients.createDefault();
		HttpHead head = new HttpHead(conf.getUri());
		CloseableHttpResponse response = client.execute(head);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != 200) {
			response.close();
			return false;
		}
		Header[] contentSizeHeaders = response.getHeaders("Content-Length");
		if (contentSizeHeaders.length > 0) {
			String contentSizeString = contentSizeHeaders[0].getValue();
			resourceSize = Long.parseLong(contentSizeString);
		} else {
			response.close();
			return false;
		}
		rangeEnabled = true;
		Header[] rangesHeaders = response.getHeaders("Accept-Ranges");
		if (rangesHeaders.length > 0
				&& rangesHeaders[0].getValue().equals("none")) {
			rangeEnabled = false;
			response.close();
			return true;
		}
		if (rangesHeaders.length > 0
				&& rangesHeaders[0].getValue().equals("bytes")) {
			rangeEnabled = true;
			response.close();
			return true;
		}
		response.close();
		// re-check range availability
		head = new HttpHead(conf.getUri());
		head.addHeader("Range", "bytes=0-" + resourceSize);
		response = client.execute(head);
		statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == 206)
			rangeEnabled = true;
		else
			rangeEnabled = false;
		response.close();

		return true;
	}

	private File createDownloadCacheFile() {
		UUID uuid = UUID.randomUUID();
		String fileName = uuid.toString();
		File tmp = new File("temp/" + fileName);

		FileHelper.makeSureFileExists(tmp);

		FileHelper.deleteOnExit(tmp);
		return tmp;
	}

	private void calculateAndStartDownloadThreads() {
		int maxThreads = this.conf.getMaxThreads();
		if (!rangeEnabled)
			maxThreads = 1;

		long chunkSize = MIN_CHUNK_PER_THREAD;
		if (maxThreads * MIN_CHUNK_PER_THREAD > resourceSize) {
			maxThreads = (int) (resourceSize / MIN_CHUNK_PER_THREAD);
		} else {
			int chunksCount = (int) (resourceSize / MIN_CHUNK_PER_THREAD);
			int chunksOfEachThread = chunksCount / maxThreads;

			chunkSize = chunksOfEachThread * MIN_CHUNK_PER_THREAD;
		}

		this.downloadThreads = new DownloadThread[maxThreads];

		for (int i = 0; i < maxThreads - 1; ++i)
			this.downloadThreads[i] = new DownloadThread(chunkSize * i,
					chunkSize * (i + 1));

		this.downloadThreads[maxThreads - 1] = new DownloadThread(chunkSize
				* (maxThreads - 1), resourceSize);

		for (DownloadThread thread : this.downloadThreads)
			if (thread != null)
				thread.start();
	}

	private void startDaemonThreads() {
		this.autoRetryThread = new AutoRetryThread();
		this.autoRetryThread.start();

		this.speedMonitorThread = new SpeedMonitorThread();
		this.speedMonitorThread.start();
	}

	private void stopDownloadThreads() {
		stop();
		this.downloadThreads = null;
	}

	private void waitUntilThreadsStop() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			log(ERROR, "Download parent thread interrupted while sleeping!");
			log(ERROR, e);
			dispatchOnError("download.stage.waitUntilThreadsStop", e);
		}
	}

	private void waitUntilDaemonThreadsStop() {
		while (this.speedMonitorThread.isAlive()
				|| this.autoRetryThread.isAlive())
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				log(ERROR, "Download parent thread interrupted while sleeping!");
				log(ERROR, e);
				dispatchOnError("download.stage.waitUntilDaemonThreadsSleep", e);
			}
	}

	private byte[] readCacheFileData() {
		return null;
	}

	private boolean downloadThreadsStopped() {
		boolean downloadPaused = false;

		synchronized (this) {
			downloadPaused = this.downloadPaused;
		}

		if (downloadPaused)
			return false;

		return areDownloadThreadsSleeping();
	}

	public boolean areDownloadThreadsSleeping() {
		return aliveDownloadThreadsCount() == 0;
	}

	public boolean areDownloadThreadsWorking() {
		return aliveDownloadThreadsCount() == this.downloadThreads.length;
	}

	public int aliveDownloadThreadsCount() {
		if (!downloading || !downloadStarted)
			return 0;

		int aliveThreadsCount = 0;
		for (DownloadThread thread : this.downloadThreads)
			if (thread != null)
				if (thread.isAlive() && thread.getMethod != null)
					++aliveThreadsCount;

		return aliveThreadsCount;
	}

	// state-control methods

	public synchronized void pauseAsync() {
		if (asyncProxy != null)
			asyncProxy.pauseAsync();
		else {
			if (!downloadStarted || downloadPaused || stopRequested)
				return;
			downloadPaused = true;
		}
	}

	public synchronized void pause() {
		if (asyncProxy != null)
			asyncProxy.pause();
		else {
			pauseAsync();

			while (!areDownloadThreadsSleeping())
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					log(ERROR, "Thread interrupted while sleeping!");
					log(ERROR, e);
				}
		}
	}

	public synchronized void resumeAsync() {
		if (asyncProxy != null)
			asyncProxy.resumeAsync();
		else {
			if (!downloadStarted || !downloadPaused || stopRequested)
				return;
			downloadPaused = false;
		}
	}

	public synchronized void resume() {
		if (asyncProxy != null)
			asyncProxy.resume();
		else {
			resumeAsync();

			while (!areDownloadThreadsWorking())
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					log(ERROR, "Thread interrupted while sleeping!");
					log(ERROR, e);
				}
		}
	}

	public synchronized void stopAsync() {
		if (asyncProxy != null)
			asyncProxy.stopAsync();
		else {
			if (!downloadStarted || stopRequested)
				return;
			stopRequested = true;
		}
	}

	public synchronized void stop() {
		if (asyncProxy != null)
			asyncProxy.stop();
		else {
			stopAsync();

			while (!areDownloadThreadsSleeping())
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					log(ERROR, "Thread interrupted while sleeping!");
					log(ERROR, e);
				}
		}
	}

	// methods that dispatches messages to the callback.

	private void dispatchOnDownloadStart() {
		try {
			if (conf.callback != null)
				conf.callback.onDownloadStart();
		} catch (Throwable t) {
			log(WARNING, "Throwable caught while dispatching to callbac!");
			log(WARNING, t);
		}
	}

	private void dispatchOnResourceAvailabilityCheckStart() {
		try {
			if (conf.callback != null)
				conf.callback.onResourceAvailabilityCheckStart();
		} catch (Throwable t) {
			log(WARNING, "Throwable caught while dispatching to callbac!");
			log(WARNING, t);
		}
	}

	private void dispatchOnResourceAvailabilityCheckEnd() {
		try {
			if (conf.callback != null)
				conf.callback.onResourceAvailabilityCheckEnd();
		} catch (Throwable t) {
			log(WARNING, "Throwable caught while dispatching to callbac!");
			log(WARNING, t);
		}
	}

	private void dispatchOnResourceUnavailable() {
		try {
			if (conf.callback != null)
				conf.callback.onResourceUnavailable();
		} catch (Throwable t) {
			log(WARNING, "Throwable caught while dispatching to callbac!");
			log(WARNING, t);
		}
	}

	private void dispatchOnDownloadThreadStart(Thread thread) {
		try {
			if (conf.callback != null)
				conf.callback.onDownloadThreadStart(thread);
		} catch (Throwable t) {
			log(WARNING, "Throwable caught while dispatching to callbac!");
			log(WARNING, t);
		}
	}

	private void dispatchOnDownloadFinished() {
		try {
			if (conf.callback != null)
				conf.callback.onDownloadFinished();
		} catch (Throwable t) {
			log(WARNING, "Throwable caught while dispatching to callbac!");
			log(WARNING, t);
		}
	}

	private void dispatchOnDownloadFailed(String message) {
		try {
			if (conf.callback != null)
				conf.callback.onDownloadFailed(message);
		} catch (Throwable t) {
			log(WARNING, "Throwable caught while dispatching to callbac!");
			log(WARNING, t);
		}
	}

	private void dispatchOnDownloadSucceed(byte[] data) {
		try {
			if (conf.callback != null)
				conf.callback.onDownloadSucceed(data);
		} catch (Throwable t) {
			log(WARNING, "Throwable caught while dispatching to callbac!");
			log(WARNING, t);
		}
	}

	private void dispatchOnCacheFileCreated(File cacheFile) {
		try {
			if (conf.callback != null)
				conf.callback.onCacheFileCreated(cacheFile);
		} catch (Throwable t) {
			log(WARNING, "Throwable caught while dispatching to callbac!");
			log(WARNING, t);
		}
	}

	private void dispatchOnError(String occasion, Throwable t) {
		try {
			if (conf.callback != null)
				conf.callback.onError(occasion, t);
		} catch (Throwable t1) {
			log(WARNING, "Throwable caught while dispatching to callbac!");
			log(WARNING, t1);
		}
	}

	private void dispatchOnDownloadThreadRestarted(Thread thread) {
		try {
			if (conf.callback != null)
				conf.callback.onDownloadThreadRestarted(thread);
		} catch (Throwable t) {
			log(WARNING, "Throwable caught while dispatching to callbac!");
			log(WARNING, t);
		}
	}

	private void dispatchOnPackageReceived(Thread thread, long offset,
			long size, byte[] buffer) {
		this.speedMonitorThread.onPackageReceived(size);

		try {
			if (conf.callback != null)
				conf.callback.onPackageReceived(thread, offset, size, buffer);
		} catch (Throwable t) {
			log(WARNING, "Throwable caught while dispatching to callbac!");
			log(WARNING, t);
		}
	}

	private void dispatchOnDownloadThreadStopped(boolean onError) {
		try {
			if (conf.callback != null)
				conf.callback.onDownloadThreadStopped(onError);
		} catch (Throwable t) {
			log(WARNING, "Throwable caught while dispatching to callbac!");
			log(WARNING, t);
		}
	}

	private void dispatchOnAutoRetryThreadStart(Thread autoRetryThread) {
		try {
			if (conf.callback != null)
				conf.callback.onAutoRetryThreadStart(autoRetryThread);
		} catch (Throwable t) {
			log(WARNING, "Throwable caught while dispatching to callbac!");
			log(WARNING, t);
		}
	}

	private void dispatchOnDownloadThreadForcedRestarted(Thread thread) {
		try {
			if (conf.callback != null)
				conf.callback.onDownloadThreadRestarted(thread);
		} catch (Throwable t) {
			log(WARNING, "Throwable caught while dispatching to callbac!");
			log(WARNING, t);
		}
	}

	private void dispatchDynamicDownloadSpeed(float lastSec, float sec10Avg,
			float totAvg) {
		try {
			if (conf.callback != null)
				conf.callback.dynamicDownloadSpeed(lastSec, sec10Avg, totAvg);
		} catch (Throwable t) {
			log(WARNING, "Throwable caught while dispatching to callbac!");
			log(WARNING, t);
		}
	}
}
