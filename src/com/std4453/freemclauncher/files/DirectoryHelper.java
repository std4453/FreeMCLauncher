package com.std4453.freemclauncher.files;

import java.io.File;

public class DirectoryHelper {
	/**
	 * root file where the launcher jar is stored.
	 */
	public static File launcherRoot = new File(System.getProperty("user.dir"));
	public static File dotMinecrftRoot = new File(launcherRoot, ".minecraft");
	public static File versions = new File(dotMinecrftRoot, "versions");
	public static File assets = new File(dotMinecrftRoot, "assets");
	public static File libraries = new File(dotMinecrftRoot, "libraries");
	public static File temp = new File(launcherRoot, "temp");
	public static File resources = new File(dotMinecrftRoot, "resources");
}
