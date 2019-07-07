package com.arrow.pegasus.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

final class RandomLoader {
	RandomLoader(InputStream is, int numLines) {
		this._numLines = numLines;
		BufferedReader reader = null;
		try {
			_junks = new String[numLines];
			reader = new BufferedReader(new InputStreamReader(is));
			int row = 0;
			String line = null;
			while ((line = reader.readLine()) != null) {
				_junks[row++] = line;
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("FATAL ERROR: SECURITY FILE IS CORRUPTED!");
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public String find(int line) {
		return _junks[trim(line)];
	}

	public char find(int a, int b) {
		char c = 0;
		for (int i = 0; i < 5; i++) {
			c = find(trim(a)).charAt(trim(b));
			b = a;
			a = c;
		}
		return c;
	}

	public int trim(int a) {
		return a % _numLines;
	}

	public int numLines() {
		return this._numLines;
	}

	private final int _numLines;
	private String[] _junks;
}