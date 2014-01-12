package cn.zhuhongqing.excel.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 简单的StringUtils类
 * 
 * 提供简单的String操作
 * 
 * @author Mr.Yi 2013/5/29 10:23
 * 
 */

public class StringUtils {

	public static final String DEFAULT_REG = ",";

	public static boolean isNull(Object obj) {
		if (obj == null) {
			return true;
		}
		if (obj instanceof String) {
			return isNull((String) obj);
		} else {
			return isNull(obj.toString());
		}
	}

	/**
	 * 判断一个字符串是否为null或者长度为0
	 * 
	 * 是则返回true,不是则false
	 * 
	 * @param str
	 * @return boolean
	 */

	public static boolean isNull(String str) {
		if (str == null || str.length() < 1) {
			return true;
		}

		return false;
	}

	/**
	 * 将一个字符串的头位变成大写
	 * 
	 * @param str
	 * @return String
	 */

	public static String firstToUpper(String str) {
		str = str.substring(0, 1).toUpperCase() + str.substring(1);
		return str;
	}

	/**
	 * 将一个字符串的头位变成小写
	 * 
	 * @param str
	 * @return String
	 */

	public static String firstToLower(String str) {
		str = str.substring(0, 1).toLowerCase() + str.substring(1);
		return str;
	}

	/**
	 * 
	 * 字符串尾部匹配
	 * 
	 * @param source
	 * @param eq
	 * @return
	 */

	public static boolean endsWithIgnoreCase(String source, String eq) {

		int temp = eq.length();

		if (eq.length() > source.length()) {
			return false;
		}

		return source.substring(source.length() - temp).equalsIgnoreCase(eq);

	}

	/**
	 * 
	 * 判断一个数组是否为null或是长度小于1
	 * 
	 * 是则返回true,不是则false.
	 * 
	 * @param str
	 * @return boolean
	 */

	public static boolean isEmpry(String[] str) {
		if (str == null || str.length < 1) {
			return true;
		}

		return false;
	}

	/**
	 * 将一个String依照reg进行截断存入一个List中,最后返回List
	 * 
	 * @param str
	 * @param reg
	 * @return List<String>
	 */

	public static List<String> splitString(String str, String reg) {

		List<String> strList = new ArrayList<String>();

		if (isNull(str)) {
			return strList;
		}

		StringTokenizer st = new StringTokenizer(str, reg);

		while (st.hasMoreElements()) {
			strList.add(st.nextToken().trim());
		}

		return strList;

	}

}
