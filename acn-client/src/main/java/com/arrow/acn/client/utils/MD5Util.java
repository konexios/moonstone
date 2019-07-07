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
package com.arrow.acn.client.utils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class MD5Util {
	public static final int BUFFER_SIZE = 8192;

	public static byte[] calcMD5Checksum(Path path) throws NoSuchAlgorithmException, IOException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		try (SeekableByteChannel sbc = Files.newByteChannel(path)) {
			ByteBuffer buf = ByteBuffer.allocateDirect(BUFFER_SIZE);
			while (sbc.read(buf) > 0) {
				buf.flip();
				md.update(buf);
				buf.clear();
			}
		}
		return md.digest();
	}

	public static byte[] calcMD5Checksum(File file) throws NoSuchAlgorithmException, IOException {
		return calcMD5Checksum(file.toPath());
	}

	public static byte[] calcMD5Checksum(String pathname) throws NoSuchAlgorithmException, IOException {
		return calcMD5Checksum(new File(pathname));
	}

	public static String calcMD5ChecksumString(Path path) throws NoSuchAlgorithmException, IOException {
		return Hex.encodeHexString(calcMD5Checksum(path));
	}

	public static String calcMD5ChecksumString(File file) throws NoSuchAlgorithmException, IOException {
		return Hex.encodeHexString(calcMD5Checksum(file));
	}

	public static String calcMD5ChecksumString(String pathname) throws NoSuchAlgorithmException, IOException {
		return Hex.encodeHexString(calcMD5Checksum(pathname));
	}
}
