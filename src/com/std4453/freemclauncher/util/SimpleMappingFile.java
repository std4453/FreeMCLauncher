package com.std4453.freemclauncher.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleMappingFile {
	private static final Logger logger = Logger
			.getLogger("com.std4453.freemclauncher.log.Logger");

	/**
	 * escapes '\', '=' and characters ASCII 00~1f by format \XX, where XX is
	 * the ASCII value of the character.
	 * 
	 * @param original
	 * @return
	 */
	public static String escape(String original) {
		String escaped = original.replace("\\", "\\5C");
		escaped = escaped.replace("=", "\\3D");

		for (int i = 0; i < 32; ++i) {
			escaped = escaped.replace("" + (char) i, "\\" + (i < 16 ? '0' : "")
					+ Integer.toHexString(i).toUpperCase());
		}

		return escaped;
	}

	/**
	 * unescapes the string escaped by {@code escape()}.<br>
	 * See {@link #escape(String)}
	 * 
	 * @param escaped
	 * @return
	 */
	public static String unescape(String escaped) {
		StringBuilder original = new StringBuilder("");
		int ch;
		for (int pointer = 0; pointer < escaped.length(); ++pointer) {
			ch = escaped.codePointAt(pointer);
			if (ch == '\\') {
				original.append((char) ((int) Integer.parseInt(
						escaped.substring(pointer + 1, pointer + 3), 16)));
				pointer += 2;
			} else {
				original.appendCodePoint(ch);
			}
		}

		return original.toString();
	}

	protected File file;

	protected Map<String, String> mapping;

	public SimpleMappingFile() {
		init();
	}

	public SimpleMappingFile(Collection<Map.Entry<String, String>> mapping) {
		this();
		if (mapping != null) {
			for (Map.Entry<String, String> entry : mapping) {
				this.mapping.put(entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * sets the {@code File} for the mapping file object to read from. if the
	 * given {@code File} is inaccessible, this argument is ignored.
	 * 
	 * @param file
	 */
	public SimpleMappingFile(File file) {
		this.file = file;
		if (!file.exists() || file.isDirectory() || !file.canRead())
			this.file = null;

		init();
	}

	public SimpleMappingFile(Map<? extends String, ? extends String> mapping) {
		this();
		if (mapping != null)
			this.mapping.putAll(mapping);
	}

	public SimpleMappingFile(SimpleMappingFile file) {
		this();

		this.file = file.file;
		this.mapping.putAll(file.mapping);
	}

	public SimpleMappingFile(String fileName) {
		this(new File(fileName));
	}

	public Set<Map.Entry<String, String>> entrySet() {
		return this.mapping.entrySet();
	}

	public File getFile() {
		return file;
	}

	public String getValue(String key) {
		return this.mapping.get(key);
	}

	public boolean hasKey(String key) {
		return this.mapping.containsKey(key);
	}

	public boolean hasValue(String value) {
		return this.mapping.containsValue(value);
	}

	/**
	 * only called internally and on initialization. Reads the mapping form the
	 * {@code file} field if it's available.<br>
	 * Ignores lines of the mapping file that doesn't meet the format.
	 */
	protected void init() {
		this.mapping = new HashMap<String, String>();
		if (this.file != null) {
			// read mapping from file
			try {
				Scanner scanner = new Scanner(file);
				String line;
				String key, value;

				while (scanner.hasNextLine()) {
					line = scanner.nextLine();
					int index = line.indexOf('=');
					if (index == -1)
						continue;

					key = line.substring(0, index);
					value = line.substring(index + 1);

					this.mapping.put(key, unescape(value));
				}

				scanner.close();
			} catch (FileNotFoundException e) {
				logger.log(Level.WARNING,
						"Given file doesn't exists, ignoring.", e);
			}
		}
	}

	public void put(String key, String value) {
		this.mapping.put(key, value);
	}

	public void setFile(File file) {
		this.file = file;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("");

		sb.append(String.format("SimpleMappingFile[\n\tfile=%s,\n\tmapping=\n",
				this.file));
		for (Map.Entry<String, String> entry : this.entrySet()) {
			sb.append(String.format("\t\t%s -> %s\n", entry.getKey(),
					entry.getValue()));
		}
		sb.append("]");

		return sb.toString();
	}

	public Collection<String> values() {
		return this.mapping.values();
	}

	/**
	 * writes to the file given by constructor or {@code setFile()}.
	 */
	public void write() {
		if (this.file == null)
			return;
		this.file.getParentFile().mkdirs();

		try {
			PrintWriter writer = new PrintWriter(file);

			for (Map.Entry<String, String> entry : this.entrySet()) {
				writer.println(String.format("%s=%s", escape(entry.getKey()),
						escape(entry.getValue())));
			}

			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			logger.log(Level.WARNING,
					"Can't open output stream to file! Stopped!", e);
		}
	}
}
