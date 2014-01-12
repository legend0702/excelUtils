package cn.zhuhongqing.excel.exception;

import java.util.Map;

import cn.zhuhongqing.excel.utils.GenericUtils;

import com.alibaba.fastjson.JSON;

/**
 * 专门处理excel异常短路机制
 * 
 * 详情请见{@link ShortCircuit}
 * 
 * @author zhq mail:qwepoidjdj(a)hotmail.com
 * @since 1.6
 */

public class ExcelShortCircuit extends ShortCircuit {

	// 配置文件的数据key
	public static final String SHORTCIRCUIT_KEY = "SHORT_CIRCUIT";

	private static final long serialVersionUID = 1L;

	/**
	 * 读取配置文件
	 * 
	 * @param propMap
	 */
	public void initSetting(Map<String, String> propMap) {
		String setting = propMap.get(SHORTCIRCUIT_KEY);
		if (setting != null) {
			initSetting(setting);
			propMap.remove(SHORTCIRCUIT_KEY);
		}
	}

	public void initSetting(String str) {
		if (!GenericUtils.isMapJson(str)) {
			return;
		}
		// 设置属性
		Options propSC = JSON
				.toJavaObject(JSON.parseObject(str), Options.class);
		if (propSC != null) {
			this.setOptions(propSC);
		}
	}
}
