package cn.zhuhongqing.excel.convert;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;

import cn.zhuhongqing.excel.exception.ExcelConvertException;
import cn.zhuhongqing.excel.utils.StringUtils;

public abstract class AbstractExcelConvert implements ExcelConvert {

	@Override
	public boolean isTrueType(Object value) {
		if (value instanceof String) {
			return true;
		}
		return false;
	}

	@Override
	public int javaToExcelType() {
		return Cell.CELL_TYPE_STRING;
	}

	@Override
	public Object javaToExcel(Object value) throws ExcelConvertException {
		return value;
	}

	@Override
	public Object excelToJava(Object value) throws ExcelConvertException {
		return value;
	}

	@Override
	public String excelFormatter() {
		return ExcelConvert.TEST_FORMATTER;
	}

	@Override
	public List<String> initProp(String initProp) {
		return StringUtils.splitString(initProp, StringUtils.DEFAULT_REG);
	}

}
