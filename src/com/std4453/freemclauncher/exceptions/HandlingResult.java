package com.std4453.freemclauncher.exceptions;

public class HandlingResult {
	public static enum ResultCode {
		THROW, IRNORE, MSGBOX;
	}

	protected ResultCode code;
	protected Object[] params;

	public HandlingResult(ResultCode code, Object... params) {
		super();
		this.code = code;
		this.params = params;
	}

	public HandlingResult(ResultCode code) {
		this.code = code;
	}

	public ResultCode getCode() {
		return code;
	}

	public Object[] getParams() {
		return params;
	}
	
	public Object getParam(int index) {
		if (params==null)
			return null;
		
		if (index<0||index>=params.length)
			return null;
		
		return params[index];
	}
}
