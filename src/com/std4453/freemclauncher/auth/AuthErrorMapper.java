package com.std4453.freemclauncher.auth;

import com.std4453.freemclauncher.util.Pair;

public class AuthErrorMapper {
	protected static String[] errorMapping = { "Method Not Allowed", null,
			"auth.error.error.methodNotAllowed",
			"auth.error.msg.methodNotAllowed", "Not Found", null,
			"auth.error.error.resourceNotFound",
			"auth.error.msg.resourceNotFound", "ForbiddenOperationException",
			"Invalid credentials. Account migrated, use e-mail as username.",
			"auth.error.error.invalid.migrated",
			"auth.error.msg.invalid.migrated", "ForbiddenOperationException",
			"Invalid credentials. Invalid username or password.",
			"auth.error.error.invalid.invalid",
			"auth.error.msg.invalid.invalid", "ForbiddenOperationException",
			"Invalid token.", "auth.error.error.invalid.token",
			"auth.error.msg.invalid.token", "IllegalArgumentException",
			"Access token already has a profile assigned.",
			"auth.error.error.alreadyAssigned",
			"auth.error.msg.alreadyAssigned", "IllegalArgumentException",
			"credentials is null", "auth.error.error.nullCredentials",
			"auth.error.msg.nullCredentials", "Unsupported Media Type", null,
			"auth.error.error.unsupportedMediaType",
			"auth.error.msg.unsupportedMediaType", };

	public static Pair<String, String> authErrorStr2ResourceStr(
			String authErrorStr, String authErrorMsg) {
		if (authErrorStr == null || authErrorStr.isEmpty())
			return new Pair<>("general.unknownError",
					"auth.error.msg.unknownError");

		authErrorStr = authErrorStr.trim();
		authErrorMsg = authErrorMsg.trim();

		for (int i = 0; i < errorMapping.length; i += 4)
			if (errorMapping[i].equals(authErrorStr))
				if (errorMapping[i + 1] == null
						|| errorMapping[i + 1].equals(authErrorMsg))
					return new Pair<>(errorMapping[i + 2], errorMapping[i + 3]);

		return new Pair<>("general.unknownError", "auth.error.msg.unknownError");
	}
}
