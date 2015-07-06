package com.std4453.freemclauncher.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import com.std4453.freemclauncher.files.FileHelper;

public class LazyFileOutputStream extends OutputStream {
	private File file;
	private OutputStream stream;

	public LazyFileOutputStream(String file) {
		this(new File(file));
	}

	public LazyFileOutputStream(File file) {
		this.file = file;
	}

	@Override
	public void write(int b) throws IOException {
		ensureStreamExists();
		stream.write(b);
	}

	@Override
	public void write(byte[] b) throws IOException {
		ensureStreamExists();
		stream.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		ensureStreamExists();
		stream.write(b, off, len);
	}

	@Override
	public void flush() throws IOException {
		ensureStreamExists();
		stream.flush();
	}

	@Override
	public void close() throws IOException {
		ensureStreamExists();
		stream.close();
	}

	private void ensureStreamExists() {
		if (stream != null)
			return;

		FileHelper.makeSureFilePathExists(file);
		stream = FileHelper.newOutputStream(file);
	}
}
