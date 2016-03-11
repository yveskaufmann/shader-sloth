package de.yvka.slothengine.utils;

import java.io.*;

public class IOUtils {

	/**
	 * End of file byte indicates the end of a input stream or reader.
	 */
	private static final int EOF = -1;

	/**
	 * Copy the content from a reader to a writer.
	 *
	 * Note: this method will not close the specified reader or writer.
	 *
	 * @param reader the reader to copy from.
	 * @param writer the writer to write the copy.
	 * @throws IOException
     */
	public static void copy(Reader reader, Writer writer) throws IOException {
		char buffer[] = new char[4 * 1024];
		int readBytes;

		while((readBytes = reader.read(buffer)) != EOF) {
			writer.write(buffer, 0, readBytes);
		}
	}

	/**
	 * Read the complete content of a Reader into
	 * a string and return it.
	 *
	 * Note this method will closed the specified reader.
	 *
	 * @param reader the reader to read from
	 * @return the content of the reader as string.
	 * @throws IOException if an io error occurred
     */
	public static String toString(Reader reader) throws IOException {
		StringWriter writer = new StringWriter();
		try {
			copy(reader, writer);
		} finally {
			closeQuietly(reader);
			closeQuietly(writer);
		}
		return writer.toString();
	}

	/**
	 * Read the complete content of a File into
	 * a string and return it.
	 *
	 * @param file the file to read from
	 * @return the content of the file as string.
	 * @throws IOException if an io error occurred
	 */
	public static String toString(File file) throws IOException {
		return toString(new FileReader(file));
	}

	/**
	 * Close a <code>Closeable quietly</code>
	 *
	 * @param closeable the <code>Closeable</code> which should be closed
     */
	public static void closeQuietly(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException ignored) {}
		}
	}
}
