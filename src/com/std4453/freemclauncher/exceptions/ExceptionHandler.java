package com.std4453.freemclauncher.exceptions;

import java.util.List;
import java.util.Vector;

import com.std4453.freemclauncher.exceptions.HandlingResult.ResultCode;

public class ExceptionHandler {
	protected List<ExceptionHandler> children;
	protected String name;

	public ExceptionHandler(String name) {
		this.name = name;
		this.children = new Vector<ExceptionHandler>();
	}

	public final void addChild(ExceptionHandler child) {
		if (child == null)
			return;

		if (onChildAdded(child))
			this.children.add(child);
	}

	protected boolean onChildAdded(ExceptionHandler child) {
		return true;
	}

	public final HandlingResult handleException(String context, Throwable t) {
		if (context == null || context.isEmpty())
			return handleException(t);

		String subname;
		String lastname = null;
		if (context.indexOf('.') == -1)
			subname = context;
		else {
			subname = context.substring(0, context.indexOf('.'));
			lastname = context.substring(context.indexOf('.') + 1);
		}

		for (ExceptionHandler handler : this.children) {
			if (handler == null || !handler.name.equals(subname))
				continue;
			else {
				HandlingResult result = handler.handleException(lastname, t);
				if (result != null)
					return result;
			}
		}

		return handleException(t);
	}

	protected HandlingResult handleException(Throwable t) {
		return new HandlingResult(ResultCode.THROW);
	}
}
