package com.std4453.freemclauncher.auth;

public interface IAuthenticator {
	public String getAuthAccessToken();
	public String getAuthSession();
	public String getUUID();
	public String getUserProperties();
	public String getPlayerName();
	public String getUserType();
}
