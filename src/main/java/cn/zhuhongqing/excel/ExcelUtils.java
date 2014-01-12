package cn.zhuhongqing.excel;

import java.util.ArrayList;
import java.util.List;

/**
 * 在用代码操作Excel的过程中,会用到把列名转化为数字,然后再进行计算确认列处理。
 * 
 * 把列名转化为数字很容易实现定位.下面的这两个方法的主要作用是:
 * 
 * (1)把字母转为数字, 如1转为A,AA转为27.然后进行处理.
 * 
 * (2)把数字转为字母,A->1,27->AA...
 * 
 * @author zhq mail:qwepoidjdj(a)hotmail.com
 * 
 */

public class ExcelUtils {

	/**
	 * 1、字母转数字
	 * 
	 * 思想： 从字符串的最后一位到第一位,乘以26的幂,依次相加.
	 * 
	 * 算法: 26^0 * (最后一位) + 26 ^ 1 * (前一位 ) + ... + 26 ^ n * (第一位).
	 * 
	 * @param value
	 * @return
	 */

	public static int strToIndex(String value) {
		int rtn = 0;
		int powIndex = 0;

		for (int i = value.length() - 1; i >= 0; i--) {
			int tmpInt = value.charAt(i);
			tmpInt -= 64;
			rtn += (int) Math.pow(26, powIndex) * tmpInt;
			powIndex++;
		}
		return rtn;
	}

	public static String IntdexToStr(int value) {
		StringBuffer rtn = new StringBuffer();
		List<Integer> iList = new ArrayList<Integer>();

		// To single Int
		while (value / 26 != 0 || value % 26 != 0) {
			iList.add(value % 26);
			value /= 26;
		}

		// Change 0 To 26
		for (int j = 0; j < iList.size(); j++) {
			if (iList.get(j) == 0) {
				iList.set(j + 1, iList.get(j + 1) - 1);
				iList.set(j, 26);
			}
		}

		// Remove 0 at last
		if (iList.get(iList.size() - 1) == 0) {
			iList.remove(iList.size() - 1);
		}

		// To String
		for (int j = (iList.size() - 1); j >= 0; j--) {
			char c = (char) (iList.get(j) + 64);
			rtn.append(c);
		}
		return rtn.toString();
	}
}
