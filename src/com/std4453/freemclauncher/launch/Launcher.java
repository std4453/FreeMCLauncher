package com.std4453.freemclauncher.launch;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import org.json.JSONObject;

import com.std4453.freemclauncher.assets.AssetsIndexDownloader;
import com.std4453.freemclauncher.auth.AuthenticatorFactory;
import com.std4453.freemclauncher.auth.IAuthenticator;
import com.std4453.freemclauncher.files.DirectoryHelper;
import com.std4453.freemclauncher.files.FileHelper;
import com.std4453.freemclauncher.profiles.Profile;
import com.std4453.freemclauncher.profiles.ProfileScanner;
import com.std4453.freemclauncher.util.StringParamReplacer;
import com.std4453.freemclauncher.util.StructuredDataHelper;
import com.std4453.freemclauncher.util.StructuredDataObject;

public class Launcher {
	public Launcher() {
	}

	public void launchNoCheck(Profile profile) throws IOException,
			InterruptedException {
		if (profile == null)
			return;

		profile.checkLibraryAvailability();

		IAuthenticator auth = AuthenticatorFactory.getAuthenticatorForVersion();

		File jsonFile = new File(DirectoryHelper.versions, String.format(
				"%s/%s.json", profile.getName(), profile.getName()));
		if (!jsonFile.exists())
			return;
		String jsonString = FileHelper.getFileContentAsString(jsonFile);
		JSONObject jsonObject = new JSONObject(jsonString);
		StructuredDataObject jsonSDO = StructuredDataHelper.fromJSONObject(
				jsonObject).toStructuredDataObject();
		String args = jsonSDO.getString("minecraftArguments");

		String auth_session = auth.getAuthSession();
		String game_assets = DirectoryHelper.assets.getAbsolutePath();
		String user_properties = auth.getUserProperties();
		String assets_root = game_assets;
		String auth_player_name = auth.getPlayerName();
		String auth_access_token = auth.getAuthAccessToken();
		String game_directory = DirectoryHelper.dotMinecrftRoot
				.getAbsolutePath();
		String assets_index_name = new AssetsIndexDownloader()
				.getAssetsIndexNameFromVersionName(profile.getName());
		String auth_uuid = auth.getUUID();
		String version_name = profile.getName();
		String user_type = auth.getUserType();

		args = StringParamReplacer.replaceParam(args, "auth_session",
				auth_session);
		args = StringParamReplacer.replaceParam(args, "game_assets",
				game_assets);
		args = StringParamReplacer.replaceParam(args, "user_properties",
				user_properties);
		args = StringParamReplacer.replaceParam(args, "assets_root",
				assets_root);
		args = StringParamReplacer.replaceParam(args, "auth_player_name",
				auth_player_name);
		args = StringParamReplacer.replaceParam(args, "auth_access_token",
				auth_access_token);
		args = StringParamReplacer.replaceParam(args, "game_directory",
				game_directory);
		args = StringParamReplacer.replaceParam(args, "assets_index_name",
				assets_index_name);
		args = StringParamReplacer.replaceParam(args, "auth_uuid", auth_uuid);
		args = StringParamReplacer.replaceParam(args, "version_name",
				version_name);
		args = StringParamReplacer.replaceParam(args, "user_type", user_type);

		String cp = LibrariesCommandLineBuilder.getJavaClassPath(profile);
		String mainClass = MainClassHelper.getMinecraftMainClass(profile);

		// TODO:calculate memory
		String memory = "512M";

		// TODO:extract natives
		Process process = new ProcessBuilder(String.format(
				"java -Xms%s -cp %s %s %s", memory, cp, mainClass, args))
				.redirectErrorStream(true).start();
		InputStream out = process.getInputStream();
		Scanner scanner = new Scanner(out);
		while (scanner.hasNextLine())
			System.out.println(scanner.nextLine());
		process.waitFor();
		scanner.close();
		process.destroy();
	}

	public static void main(String[] args) throws IOException,
			InterruptedException {
		ProfileScanner scanner = new ProfileScanner();
		scanner.scanForProfiles();
		List<Profile> profiles = scanner.getProfiles();
		Profile profile = null;
		for (Profile profile1 : profiles)
			if (profile1.getName().equals("1.5.2"))
				profile = profile1;

		new Launcher().launchNoCheck(profile);
	}
}
