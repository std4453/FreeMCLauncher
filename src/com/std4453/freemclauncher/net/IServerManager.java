package com.std4453.freemclauncher.net;

import java.io.OutputStream;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;

import com.std4453.freemclauncher.net.URLDownloader.OnDownloadProgressChangedListener;
import com.std4453.freemclauncher.net.URLDownloader.OnDownloadSpeedChangedListener;

public interface IServerManager {
	public CloseableHttpResponse sendRequestGET(Server server,
			Map<String, String> data) throws Exception;

	public CloseableHttpResponse sendRequestPOST(Server server,
			Map<String, String> data) throws Exception;

	public byte[] downloadFromServer(Server server, Map<String, String> data)
			throws Exception;

	public void downloadFromServer(Server server, Map<String, String> data,
			OnDownloadProgressChangedListener downloadProgressChangedListener,
			OnDownloadSpeedChangedListener downloadSpeedChangedListener,
			OutputStream dst) throws Exception;
	
	public String getDownloadURL(Server server,Map<String,String> data);
}