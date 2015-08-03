package com.std4453.freemclauncher.net;

public class ServerManagerFactory {
	private static IServerManager serverManager;
	
	public static IServerManager getServerManager() {
		if (serverManager==null)
			initServerManagerInternal();
		return serverManager;
	}
	
	private static void initServerManagerInternal() {
		//use BMCLAPI in China to get a far better speed
		serverManager=new ServerManagerBMCLAPI();
	}
}
