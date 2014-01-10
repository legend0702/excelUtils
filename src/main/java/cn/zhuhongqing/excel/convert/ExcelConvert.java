package cn.zhuhongqing.excel.convert;

import java.util.List;

import cn.zhuhongqing.excel.exception.ExcelConvertException;

/**
 * excel转换成java
 * 
 * 主要包含、值转换、类型检测、格式化功能
 * 
 * @author zhq mail:qwepoidjdj(a)hotmail.com
 * @version $Id$
 * @since 1.6
 */

public interface ExcelConvert {
	// 文本类型格式化
	public static final String TEST_FORMATTER = "@";
	// 日期类型格式化
	public static final String DATE_FORMATTER = "yyyy/m/d;@";

	// 数字类型格式化
	// 常规
	public static final String NUMBER_FORMATTER = "General";

	// public static final String NUMBER_FORMATTER = "@";

	// 校验数据类型
	boolean isTrueType(Object value);

	// 初始化验证属性
	// 返回验证字段
	List<String> initProp(String initProp);

	// 从excel转换到java类型
	Object excelToJava(Object value) throws ExcelConvertException;

	// 从java类型转换到excel类型
	Object javaToExcel(Object value) throws ExcelConvertException;

	// 从java到excel类型
	int javaToExcelType();

	// excel格式化
	String excelFormatter();

}
