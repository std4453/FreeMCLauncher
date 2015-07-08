package com.std4453.freemclauncher.exceptions;

public class ResultHandler {
	public static void HandleResult(HandlingResult result, String context,
			Throwable t) {
		//TODO: handle result
		
		switch (result.getCode()) {
		case IRNORE:
			break;
		case MSGBOX:
			break;
		case THROW:
			throw new RuntimeException("Exception thrown with context "
					+ context, t);
		default:
			break;

		}
	}
}
