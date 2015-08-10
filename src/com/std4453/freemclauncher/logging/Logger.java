package com.std4453.freemclauncher.logging;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class Logger {
	public static final String INFO = "INFO";
	public static final String VERBOSE = "VERBOSE";
	public static final String DEBUG = "DEBUG";
	public static final String WARNING = "WARNING";
	public static final String ERROR = "ERROR";

	public static final boolean DEBUG_FLAG = true;

	private static PrintStream out = System.out;

	public static void setOutput(PrintStream out) {
		Logger.out = out;
	}

	public static PrintStream getOutput() {
		return out;
	}

	public static void log(String prefix, String msg) {
		if (prefix == null)
			prefix = INFO;

		log(getLogString(prefix, msg));
	}

	private static void log(Iterable<String> msg) {
		if (out != null) {
			for (String str : msg)
				out.println(str);
		}
	}

	private static Iterable<String> getLogString(String prefix, String msg) {
		List<String> str = new Vector<String>();

		StringBuilder builder = new StringBuilder();

		if (DEBUG_FLAG) {
			StackTraceElement[] stackTraces = Thread.currentThread()
					.getStackTrace();
			if (stackTraces.length >= 5) {
				StackTraceElement stackTrace = stackTraces[4];
				if (stackTrace != null) {
					str.add(appendPrefix(builder, prefix).append(
							String.format("at %s.%s(%s:%d)",
									stackTrace.getClassName(),
									stackTrace.getMethodName(),
									stackTrace.getFileName(),
									stackTrace.getLineNumber())).toString());
					builder.setLength(0);
				}
			}
		}

		for (String line : msg.split("\n")) {
			str.add(appendPrefix(builder, prefix).append(line).toString());
			builder.setLength(0);
		}

		return str;
	}

	public static void log(String prefix, Throwable t) {
		if (prefix == null)
			prefix = INFO;

		StringBuilder builder = new StringBuilder();

		if (DEBUG_FLAG) {
			StackTraceElement[] stackTraces = Thread.currentThread()
					.getStackTrace();
			if (stackTraces.length >= 3) {
				StackTraceElement stackTrace = stackTraces[2];
				if (stackTrace != null) {
					out.println(appendPrefix(builder, prefix).append(
							String.format("at %s.%s(%s:%d)",
									stackTrace.getClassName(),
									stackTrace.getMethodName(),
									stackTrace.getFileName(),
									stackTrace.getLineNumber())));
					builder.setLength(0);
				}
			}
		}

		if (t == null) {
			out.println(appendPrefix(builder, prefix).append(
					String.format("Exception in thread \"%s\" null", Thread
							.currentThread().getName())));
			return;
		}

		appendPrefix(builder, prefix).append(
				String.format("Exception in thread \"%s\" ", Thread
						.currentThread().getName()));

		t.printStackTrace(new PrintStream(new StackTraceOutputStream(prefix,
				builder)));

		out.println(builder);
	}

	private static StringBuilder appendPrefix(StringBuilder builder,
			String prefix) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return builder.append(String.format("[%s][%s] ",
				format.format(new Date()), prefix));
	}

	private static final class StackTraceOutputStream extends OutputStream {
		private StringBuilder builder;
		private String prefix;

		public StackTraceOutputStream(String prefix, StringBuilder builder) {
			this.builder = builder;
			this.prefix = prefix;
		}

		@Override
		public void write(int b) throws IOException {
			if (b == '\t')
				appendPrefix(builder, prefix);
			builder.appendCodePoint(b);
		}
	}
}
