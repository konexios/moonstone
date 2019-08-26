/*******************************************************************************
 * Copyright (c) 2018 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Arrow Electronics, Inc.
 *******************************************************************************/
package moonstone.acs;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public final class AcsUtils {
	public final static String EMPTY_TRING = "";
	public final static String HMAC_SHA_256 = "HmacSha256";
	public final static String SHA_256 = "SHA-256";

	public static boolean isEmpty(final CharSequence cs) {
		return cs == null || cs.length() == 0;
	}

	public static boolean isNotEmpty(final CharSequence cs) {
		return cs != null && cs.length() > 0;
	}

	public static <T extends CharSequence> T notEmpty(final T chars, final String message, final Object... values) {
		if (chars == null) {
			throw new NullPointerException(String.format(message, values));
		}
		if (chars.length() == 0) {
			throw new IllegalArgumentException(String.format(message, values));
		}
		return chars;
	}

	public static <T> T notNull(final T object, final String message, final Object... values) {
		if (object == null) {
			throw new NullPointerException(String.format(message, values));
		}
		return object;
	}

	public static String trimToEmpty(final String str) {
		return str == null ? EMPTY_TRING : str.trim();
	}

	public static String trimToNull(final String str) {
		String temp = trimToEmpty(str);
		return temp.equals(EMPTY_TRING) ? null : temp;
	}

	public static String getStackTrace(final Throwable throwable) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw, true);
		throwable.printStackTrace(pw);
		return sw.getBuffer().toString();
	}

	public static String streamToString(final InputStream is, final Charset charset) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = is.read(buffer)) != -1) {
			bos.write(buffer, 0, length);
		}
		return bos.toString(charset.name());
	}

	public static List<String> streamToLines(final InputStream is, final Charset charset) throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset))) {
			return reader.lines().collect(Collectors.toList());
		}
	}

	public static long fastCopy(final InputStream src, File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		try {
			return fastCopy(src, fos);
		} finally {
			fos.close();
		}
	}

	public static long fastCopy(final InputStream src, final OutputStream dest) throws IOException {
		final ReadableByteChannel inputChannel = Channels.newChannel(src);
		final WritableByteChannel outputChannel = Channels.newChannel(dest);
		return fastCopy(inputChannel, outputChannel);
	}

	public static void close(InputStream is) {
		try {
			if (is != null)
				is.close();
		} catch (Throwable t) {
		}
	}

	public static void close(Closeable closeable) {
		try {
			if (closeable != null)
				closeable.close();
		} catch (Throwable t) {
		}
	}

	public static String printHex(byte[] data) {
		return DatatypeConverter.printHexBinary(data).toLowerCase();
	}

	public static String hmacSha256Hex(String key, String data) {
		try {
			SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), HMAC_SHA_256);
			Mac mac = Mac.getInstance(HMAC_SHA_256);
			mac.init(signingKey);
			return printHex(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
		} catch (Exception e) {
			throw new AcsRuntimeException("error while calculating: " + HMAC_SHA_256, e);
		}
	}

	public static String sha256Hex(String data) {
		try {
			return printHex(MessageDigest.getInstance(SHA_256).digest(data.getBytes(StandardCharsets.UTF_8)));
		} catch (Exception e) {
			throw new AcsRuntimeException("error while calculating: " + SHA_256, e);
		}
	}

	public static byte[] gzip(String str) {
		if (AcsUtils.isEmpty(str)) {
			return null;
		}
		return gzip(str.getBytes(StandardCharsets.UTF_8));
	}

	public static byte[] gzip(byte[] input) {
		if (input == null || input.length == 0) {
			return input;
		}
		ByteArrayOutputStream bos = null;
		GZIPOutputStream gos = null;
		try {
			bos = new ByteArrayOutputStream();
			gos = new GZIPOutputStream(bos);
			gos.write(input);
			return bos.toByteArray();
		} catch (Throwable t) {
			throw new AcsRuntimeException("gzip error", t);
		} finally {
			AcsUtils.close(gos);
			AcsUtils.close(bos);
		}
	}

	public static void sleepSecs(long secs) {
		try {
			Thread.sleep(secs * 1000);
		} catch (Exception e) {
		}
	}

	private static long fastCopy(final ReadableByteChannel src, final WritableByteChannel dest) throws IOException {
		final ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);

		long size = 0;
		int read = 0;
		while ((read = src.read(buffer)) != -1) {
			size += read;
			buffer.flip();
			dest.write(buffer);
			buffer.compact();
		}

		buffer.flip();

		while (buffer.hasRemaining()) {
			dest.write(buffer);
		}

		return size;
	}
}
