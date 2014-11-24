package com.jdbc.util;

/**
 * String Util
 * @author Administrator
 * 2014年11月24日
 */
public class StringUtil {

	public static boolean isEmpty(String str, boolean isTrim) {
		if (isNull(str)) {
			return false;
		}
		if (isTrim) {
			return str.trim().length() == 0;
		} else {
			return str.length() == 0;
		}
	}

	public static boolean isEmpty(String str) {
		return isEmpty(str, true);
	}

	public static boolean isNull(String str) {
		return null == str;
	}
	
}
