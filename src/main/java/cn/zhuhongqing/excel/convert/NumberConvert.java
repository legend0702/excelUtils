package cn.zhuhongqing.excel.convert;

import org.apache.poi.ss.usermodel.Cell;

import cn.zhuhongqing.excel.exception.ExcelConvertException;
import cn.zhuhongqing.excel.utils.GenericUtils;

public class NumberConvert extends AbstractExcelConvert {

	@Override
	public Object excelToJava(Object value) throws ExcelConvertException {

		if (isTrueType(value)) {
			if (value instanceof Number) {
				return value;
			}
			if (value instanceof String) {
				Number returnNumber = GenericUtils.createNumber(value
						.toString());
				return returnNumber;
			}
		}
		throw new ExcelConvertException("数字类型数据转换失败");
	}

	@Override
	public String excelFormatter() {
		return ExcelConvert.NUMBER_FORMATTER;
	}

	@Override
	public boolean isTrueType(Object value) {
		if (value instanceof Number) {
			return true;
		}
		if (value instanceof String) {
			return GenericUtils.isNumber(value.toString());
		}
		return false;
	}

	@Override
	public int javaToExcelType() {
		return Cell.CELL_TYPE_NUMERIC;
	}

}
