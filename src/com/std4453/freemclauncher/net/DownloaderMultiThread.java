package com.std4453.freemclauncher.net;

import java.io.File;

import com.std4453.freemclauncher.files.FileHelper;
import com.zywang.http.update.DownloadTask;
import com.zywang.http.update.DownloadTaskEvent;
import com.zywang.http.update.DownloadTaskListener;


public class DownloaderMultiThread implements DownloadTaskListener{
	private static final int MAX_THREADS=10;
	
	protected String url;
	protected String fileName;
	
	public DownloaderMultiThread(String url,File file) {
		this.url=url;
		this.fileName=FileHelper.getAbsoluteFileName(file);
	}
	
	public void run() {
		DownloadTask task=new DownloadTask(url, fileName,MAX_THREADS);
		task.addTaskListener(this);
		task.setAutoCallbackSleep(10);
		DownloadTask.setDebug(true);
		try {
			task.startDown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void autoCallback(DownloadTaskEvent arg0) {
	}
}
