package com.std4453.freemclauncher.net;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.std4453.freemclauncher.net.URLDownloader.OnDownloadProgressChangedListener;
import com.std4453.freemclauncher.net.URLDownloader.OnDownloadSpeedChangedListener;

public class ServerManagerBMCLAPI implements IServerManager {
	private static final Map<String, String> defaultEmptyMap = new HashMap<String, String>();

	protected static String getServerDomain(Server server) {
		switch (server) {
		case ASSETS_INDEX_SERVER:
			return "http://bmclapi2.bangbang93.com/";
		case ASSETS_SERVER:
			return "http://bmclapi2.bangbang93.com/assets/";
		case LIBRARIES_SERVER:
			return "https://libraries.minecraft.net/";
		case MINECRAFT_DOWNLOAD_SERVER:
			return "http://bmclapi2.bangbang93.com/";
		case VERSION_INDEX_SERVER:
			return "http://s3.amazonaws.com/Minecraft.Download/";
		case OLD_ASSETS_SERVER:
			return "http://s3.amazonaws.com/MinecraftResources/";
		case OLD_ASSETS_INDEX_SERVER:
			return "http://s3.amazonaws.com/MinecraftResources/";
		default:
			return null;
		}
	}

	protected String getRequestUri(Server server, Map<String, String> data) {
		if (data == null)
			data = defaultEmptyMap;

		String rootServerDomain = getServerDomain(server);

		switch (server) {
		case ASSETS_INDEX_SERVER:
			String assets_index_server_assetsIndexName = data
					.remove("indexName");
			return rootServerDomain + "indexes/"
					+ assets_index_server_assetsIndexName + ".json";
		case ASSETS_SERVER:
			String assets_server_assetHash = data.remove("assetHash");
			return rootServerDomain + assets_server_assetHash.substring(0, 2)
					+ "/" + assets_server_assetHash;
		case LIBRARIES_SERVER:
			String libraries_server_libraryPath = data.remove("libraryPath");
			return rootServerDomain + libraries_server_libraryPath;
		case MINECRAFT_DOWNLOAD_SERVER:
			String minecraft_download_server_version = data.remove("version");
			String minecraft_download_server_fileType = data.remove("fileType");
			return rootServerDomain + "versions/"
					+ minecraft_download_server_version + "/"
					+ minecraft_download_server_version + "."
					+ minecraft_download_server_fileType.toLowerCase();
		case VERSION_INDEX_SERVER:
			return rootServerDomain + "versions/versions.json";
		case OLD_ASSETS_INDEX_SERVER:
			return rootServerDomain;
		case OLD_ASSETS_SERVER:
			String old_assets_server_path = data.remove("assetPath");
			return rootServerDomain + old_assets_server_path;
		default:
			return null;
		}
	}

	protected String getRequestEncodedUrl(Server server,
			Map<String, String> data) {
		if (data == null || data.size() == 0)
			return "";

		URIBuilder builder = new URIBuilder();
		builder.setCharset(Charset.forName("UTF-8"));
		for (Map.Entry<String, String> entry : data.entrySet()) {
			if (entry == null || entry.getKey() == null
					|| entry.getValue() == null)
				continue;
			builder.addParameter(entry.getKey(), entry.getValue());
		}
		try {
			return builder.build().toString().substring(1);
		} catch (URISyntaxException e) {
			return null;
		}
	}

	@Override
	public CloseableHttpResponse sendRequestGET(Server server,
			Map<String, String> data) throws ClientProtocolException,
			IOException {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet get = new HttpGet(getRequestUri(server, data)
				+ (data == null || data.size() == 0 ? "" : "?")
				+ getRequestEncodedUrl(server, data));
		return client.execute(get);
	}

	@Override
	public CloseableHttpResponse sendRequestPOST(Server server,
			Map<String, String> data) throws ClientProtocolException,
			IOException {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(getRequestUri(server, data));
		HttpEntity entity = new StringEntity(
				getRequestEncodedUrl(server, data), ContentType.create(
						"application/x-www-form-urlencoded", "UTF-8"));
		post.setEntity(entity);
		return client.execute(post);
	}

	@Override
	public byte[] downloadFromServer(Server server, Map<String, String> data)
			throws IOException {
		CloseableHttpResponse response = sendRequestGET(server, data);
		return EntityUtils.toByteArray(response.getEntity());
	}

	@Override
	public void downloadFromServer(Server server, Map<String, String> data,
			OnDownloadProgressChangedListener downloadProgressChangedListener,
			OnDownloadSpeedChangedListener downloadSpeedChangedListener,
			OutputStream dst) throws Exception {
		String uri = getRequestUri(server, data)
				+ (data == null || data.size() == 0 ? "" : "?")
				+ getRequestEncodedUrl(server, data);
		URLDownloader.download(uri, downloadProgressChangedListener,
				downloadSpeedChangedListener, dst);

	}

	@Override
	public String getDownloadURL(Server server, Map<String, String> data) {
		return getRequestUri(server, data)
				+ (data == null || data.size() == 0 ? "" : "?")
				+ getRequestEncodedUrl(server, data);
	}
}
