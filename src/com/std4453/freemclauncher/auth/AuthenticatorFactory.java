package com.std4453.freemclauncher.auth;


public class AuthenticatorFactory {
	public static IAuthenticator getAuthenticatorForVersion() {
		//TODO: return a authenticator for the given version type.
		//i.e. AuthenticatorLegacy or AuthenticatorYggdrasil
		
		//Temporarily returns a offline authenticator
//		return new AuthenticatorOffline();
		return new AuthenticatorStd4453Test();
	}
}
