package com.std4453.freemclauncher.net;

public class ServerManagerFactory {
	private static IServerManager serverManager;
	
	public static IServerManager getServerManager() {
		if (serverManager==null)
			initServerManagerInternal();
		return serverManager;
	}
	
	private static void initServerManagerInternal() {
		serverManager=new ServerManagerDefault();
	}
}
