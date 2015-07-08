package com.std4453.freemclauncher.exceptions;

public class HandlerManager {
	protected static ExceptionHandler root;

	static {
		initHandlers();
	}

	protected static void initHandlers() {
		// TODO: add ExceptionHandlers
	}

	public static void handleException(String context, Throwable t) {
		if (root != null) {
			HandlingResult result = root.handleException(context, t);
			if (result != null)
				ResultHandler.HandleResult(result, context, t);
		} else
			throw new RuntimeException("Exception thrown with contact"
					+ context, t);
	}
}
