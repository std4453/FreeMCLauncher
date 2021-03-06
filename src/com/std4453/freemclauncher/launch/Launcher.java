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
import com.std4453.freemclauncher.util.CallbackManager;
import com.std4453.freemclauncher.util.StringParamReplacer;
import com.std4453.freemclauncher.util.StructuredDataHelper;
import com.std4453.freemclauncher.util.StructuredDataObject;
import com.std4453.freemclauncher.util.WeighableEnum;

public class Launcher {
	public static enum LaunchStates implements WeighableEnum {
		STATE_GET_AUTHENTICATOR("gui.action.launch.getAuthenticator", 2), STATE_CHECK_LIBRARIES(
				"gui.action.launch.checkLibraries", 1), STATE_PARSE_PROFILE_JSON(
				"gui.action.launch.parseProfileJSON", 1), STATE_EXTRACT_NATIVES(
				"gui.action.launch.extractNatives", 2), STATE_LAUNCH_GAME(
				"gui.action.launch.launchGame", 3);

		protected final String resName;
		protected final int weight;

		private LaunchStates(String resName, int weight) {
			this.resName = resName;
			this.weight = weight;
		}

		public String getResName() {
			return resName;
		}

		public int getWeight() {
			return weight;
		}

		@Override
		public int getTotal() {
			int total = 0;

			for (LaunchStates state : LaunchStates.values())
				total += state.getWeight();

			return total;
		}
	}

	protected CallbackManager<LaunchStates> callbackManager;

	public CallbackManager<LaunchStates> getCallbackManager() {
		return callbackManager;
	}

	public Launcher() {
		this.callbackManager = new CallbackManager<>();
	}

	public void launchNoCheck(Profile profile) throws IOException,
			InterruptedException {
		callbackManager.broadcast(LaunchStates.STATE_GET_AUTHENTICATOR);
		IAuthenticator auth = AuthenticatorFactory
				.getAuthenticatorForVersion(profile);
		launchNoCheck(profile, auth);
	}

	public void launchNoCheck(Profile profile, IAuthenticator auth)
			throws IOException, InterruptedException {
		if (profile == null)
			return;

		callbackManager.broadcast(LaunchStates.STATE_CHECK_LIBRARIES);
		profile.checkLibraryAvailability();

		callbackManager.broadcast(LaunchStates.STATE_PARSE_PROFILE_JSON);
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
		String game_assets = AssetsDirHelper.getAssetsDir(profile);
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

		callbackManager.broadcast(LaunchStates.STATE_EXTRACT_NATIVES);
		NativesExtracter extracter = new NativesExtracter();
		String nativesDir = extracter.extractNativesForProfile(profile);

		callbackManager.broadcast(LaunchStates.STATE_LAUNCH_GAME);
		Process process = new ProcessBuilder("cmd", "/c", String.format(
				"javaw -Djava.library.path=%s -Xms%s -cp %s %s %s", nativesDir,
				memory, cp, mainClass, args)).redirectErrorStream(true).start();
		InputStream out = process.getInputStream();
		Scanner scanner = new Scanner(out);
		while (scanner.hasNextLine())
			System.out.println(scanner.nextLine());
		process.waitFor();
		scanner.close();
		process.destroy();

		FileHelper.deleteOnExit(new File(nativesDir));

		callbackManager.finish();
	}

	public static void main(String[] args) throws IOException,
			InterruptedException {
		ProfileScanner scanner = new ProfileScanner();
		scanner.scanForProfiles();
		List<Profile> profiles = scanner.getProfiles();
		Profile profile = null;
		for (Profile profile1 : profiles)
			if (profile1.getName().equals("1.7.2"))
				profile = profile1;

		new Launcher().launchNoCheck(profile);
	}
}
