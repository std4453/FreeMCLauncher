package com.std4453.freemclauncher.users;

public class User {
	protected LoginType type;
	protected String userName;
	protected String password;

	public User(LoginType type, String userName, String password) {
		this.type = type;
		this.userName = userName;
		this.password = password;
	}

	public User(LoginType type, String userName) {
		this(type, userName, null);
	}

	public LoginType getType() {
		return type;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}
}
