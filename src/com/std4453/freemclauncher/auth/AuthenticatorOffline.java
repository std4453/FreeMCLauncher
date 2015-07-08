package com.std4453.freemclauncher.auth;

public class AuthenticatorOffline implements IAuthenticator{
	@Override
	public String getAuthAccessToken() {
		return "ffffffffffffffffffffffffffffffff";
	}

	@Override
	public String getAuthSession() {
		return "ffffffffffffffffffffffffffffffff";
	}

	@Override
	public String getUUID() {
		return "ffffffffffffffffffffffffffffffff";
	}

	@Override
	public String getUserProperties() {
		return "{}";
	}

	@Override
	public String getPlayerName() {
		return "std4453";
	}

	@Override
	public String getUserType() {
		return "mojang";
	}
}
