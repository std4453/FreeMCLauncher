package com.std4453.freemclauncher.auth;

import com.std4453.freemclauncher.profiles.Profile;


public class AuthenticatorFactory {
	public static IAuthenticator getAuthenticatorForVersion(Profile profile) {
		//TODO: return a authenticator for the given version type.
		//i.e. AuthenticatorLegacy or AuthenticatorYggdrasil
		
		//Temporarily returns a offline authenticator
//		return new AuthenticatorOffline();
		return new AuthenticatorStd4453Test();
	}
}
