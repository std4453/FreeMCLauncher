package com.std4453.freemclauncher.versions;

import java.io.File;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

import com.std4453.freemclauncher.files.FileHelper;
import com.std4453.freemclauncher.net.IServerManager;
import com.std4453.freemclauncher.net.Server;
import com.std4453.freemclauncher.net.ServerManagerFactory;
import com.std4453.freemclauncher.util.StructuredDataArray;
import com.std4453.freemclauncher.util.StructuredDataHelper;
import com.std4453.freemclauncher.util.StructuredDataObject;
import com.std4453.freemclauncher.util.TimeHelper;

public class VersionIndexer {
	private static final Logger logger = Logger
			.getLogger("com.std4453.freemclauncher.log.Logger");

	protected List<Version> versions;
	protected Version latestRelease;
	protected Version latestSnapshot;

	public VersionIndexer() {
		this.versions = new Vector<Version>();
	}

	public void loadVersionIndex() {
		this.versions.clear();
		this.latestRelease = null;
		this.latestSnapshot = null;

		byte[] versionListByteArray = null;
		String versionListString;

		IServerManager manager = ServerManagerFactory.getServerManager();
		try {
			versionListByteArray = manager.downloadFromServer(
					Server.VERSION_INDEX_SERVER, null);
		} catch (Exception e) {
			logger.log(Level.WARNING,
					"Exception caught while loading version index.", e);
			return;
		}

		versionListString = new String(versionListByteArray);

		StructuredDataObject data = StructuredDataHelper.fromJSONObject(
				new JSONObject(versionListString)).toStructuredDataObject();

		StructuredDataObject latest = data.getStructuredDataObject("latest");
		StructuredDataArray versions = data.getStructuredDataArray("versions");

		StructuredDataObject versionSDO;
		Version version;
		for (Object versionObject : versions) {
			versionSDO = (StructuredDataObject) versionObject;
			version = new Version(versionSDO.getString("id"),
					ReleaseType.fromString(versionSDO.getString("id"),
							versionSDO.getString("type")), VersionType.NEW,
					TimeHelper.getTime(versionSDO.getString("time")),
					TimeHelper.getTime(versionSDO.getString("releaseTime")));
			this.versions.add(version);
		}

		this.latestRelease = searchForVersion(latest.getString("release"));
		this.latestSnapshot = searchForVersion(latest.getString("snapshot"));
	}

	public void loadVersionIndexFromFile(File file) {
		String versionListString = FileHelper.getFileContentAsString(file);

		StructuredDataObject data = StructuredDataHelper.fromJSONObject(
				new JSONObject(versionListString)).toStructuredDataObject();

		StructuredDataObject latest = data.getStructuredDataObject("latest");
		StructuredDataArray versions = data.getStructuredDataArray("versions");

		StructuredDataObject versionSDO;
		Version version;
		for (Object versionObject : versions) {
			versionSDO = (StructuredDataObject) versionObject;
			version = new Version(versionSDO.getString("id"),
					ReleaseType.fromString(versionSDO.getString("id"),
							versionSDO.getString("type")), VersionType.NEW,
					TimeHelper.getTime(versionSDO.getString("time")),
					TimeHelper.getTime(versionSDO.getString("releaseTime")));
			this.versions.add(version);
		}

		this.latestRelease = searchForVersion(latest.getString("release"));
		this.latestSnapshot = searchForVersion(latest.getString("snapshot"));
	}

	private Version searchForVersion(String name) {
		for (Version version : this.versions) {
			if (version == null)
				continue;
			if (version.getVersionName().equals(name))
				return version;
		}

		return null;
	}

	public List<Version> getVersions() {
		return versions;
	}

	public static void main(String[] args) {
		VersionIndexer indexer = new VersionIndexer();
		indexer.loadVersionIndex();
		System.out.println(indexer.latestRelease);
		System.out.println(indexer.latestSnapshot);
		System.out.println();
		for (Version version : indexer.getVersions())
			System.out.println(version);
	}

	public Version getLatestRelease() {
		return latestRelease;
	}

	public Version getLatestSnapshot() {
		return latestSnapshot;
	}
}
