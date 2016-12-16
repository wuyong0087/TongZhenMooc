package com.lib.play_rec.utils;

import java.util.ArrayList;

public class HexUtil {

	/** 整形颜色值转成6位的16进制字符串格式形如（0xFFFFFF） */
	public static String colorToHexString(int t) {
		String hex = Integer.toHexString(t).toUpperCase();
		if (!"0".equals(hex)) {
			hex = hex.substring(2, hex.length());
			hex = "0x" + hex;
		}
		return hex;
	}

	/** 16进制字符串转成整形颜色值 */
	public static int hexToColor(String hex) {
		if (!"0".equals(hex)) {
			hex = hex.substring(2, hex.length());
		}
		if (hex.length() == 6) {
			hex = "FF" + hex;
		}

		long value = Long.parseLong(hex, 16);
		long max = (long) Math.pow(2, 31);

		if (value > max) {
			long v2 = (long) Math.pow(2, 32);
			value -= v2;
		}
		return (int) value;
	}
}
