package cn.zhuhongqing.excel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.zhuhongqing.excel.convert.DateConvert;
import cn.zhuhongqing.excel.convert.ExcelConvert;
import cn.zhuhongqing.excel.convert.ExcelConvertException;
import cn.zhuhongqing.excel.convert.NumberConvert;
import cn.zhuhongqing.excel.utils.StringUtils;

/**
 * excel类型转换中心
 * 
 * 支持导入导出的类型控制
 * 
 * 暂且支持String,Date,Number
 * 
 * @author zhq mail:qwepoidjdj(a)hotmail.com
 * 
 */
public class ExcelConvertCenter {

	// 复数用","分割的数据 将用来日期格式转换 date1,date2
	public static final String EXCEL_DATE = "DATE_INCLUDE";
	// 复数用","分割的数据 将用来数字格式转换 number1,number2
	public static final String EXCEL_NUMBER = "NUMBER_INCLUDE";
	// 存放全局类型转换器
	private static Map<String, ExcelConvert> globalRegisterConverts = new HashMap<String, ExcelConvert>();
	// 存放单实例的类型转换器
	private Map<String, ExcelConvert> registerConverts = new HashMap<String, ExcelConvert>();

	// 注册全局转换key:转换器
	static {
		// 日期类型注册
		registerGlobalConvert(EXCEL_DATE, new DateConvert());
		// 数字类型注册
		registerGlobalConvert(EXCEL_NUMBER, new NumberConvert());
	}

	// 转换列表
	// key就是需要转换的列名
	// value就是转换器
	private Map<String, ExcelConvert> excelConverts = new HashMap<String, ExcelConvert>();

	// 必须传入配置
	private ExcelConvertCenter() {
		registerConverts.putAll(globalRegisterConverts);
	}

	public static ExcelConvertCenter createExcelOperationCenter(
			Map<String, String> comparMap) {
		ExcelConvertCenter excelOperationCenter = new ExcelConvertCenter();
		excelOperationCenter.initExcelConverts(comparMap);
		return excelOperationCenter;
	}

	// 初始化转换中心
	private void initExcelConverts(Map<String, String> comparMap) {
		// 遍历注册列表
		Iterator<Entry<String, ExcelConvert>> registerItr = registerConverts
				.entrySet().iterator();
		while (registerItr.hasNext()) {
			Entry<String, ExcelConvert> registerEntry = registerItr.next();
			// 得到需要进行格式转换的数据
			String keys = comparMap.get(registerEntry.getKey());
			if (!StringUtils.isNull(keys)) {
				ExcelConvert convert = registerEntry.getValue();
				List<String> keyList = convert.initProp(keys);
				for (String key : keyList) {
					addExcelConverts(key, convert);
				}
				// 删掉转换key
				comparMap.remove(registerEntry.getKey());
			}
		}
	}

	// 类型检测
	public boolean checkType(String key, Object value) {
		ExcelConvert excelConvert = excelConverts.get(key);
		if (excelConvert == null) {
			return true;
		}
		return excelConvert.isTrueType(value);
	}

	// 从excel类型转换到java类型
	public Object excelToJava(String key, Object value)
			throws ExcelConvertException {
		ExcelConvert excelConvert = excelConverts.get(key);
		if (excelConvert == null) {
			return value;
		}
		return excelConvert.excelToJava(value);
	}

	// excel格式化
	public String excelFormatter(String key) {
		ExcelConvert excelConvert = excelConverts.get(key);
		if (excelConvert == null) {
			// 默认文本
			return ExcelConvert.TEST_FORMATTER;
		}
		return excelConvert.excelFormatter();
	}

	// 注册全局转换器
	private static void registerGlobalConvert(String key,
			ExcelConvert excelConvert) {
		globalRegisterConverts.put(key, excelConvert);
	}

	// 注册局部转换器
	public void registerConvert(String key, ExcelConvert excelConvert) {
		registerConverts.put(key, excelConvert);
	}

	// 添加转换器
	private void addExcelConverts(String key, ExcelConvert excelConvert) {
		excelConverts.put(key, excelConvert);
	}

}
