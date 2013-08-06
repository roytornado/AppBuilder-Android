package com.midland.base.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.res.Resources;

public class AssetsHelper {

	public static String readXML(Resources res, String file) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(res.getAssets().open(file)));
			String line;
			StringBuilder buffer = new StringBuilder();
			while ((line = in.readLine()) != null)
				buffer.append(line).append('\n');
			return buffer.toString();
		} catch (IOException e) {
			return "";
		} finally {
			closeStream(in);
		}
	}

	private static void closeStream(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
			}
		}
	}
}
