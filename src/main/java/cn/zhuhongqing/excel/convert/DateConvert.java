package cn.zhuhongqing.excel.convert;

import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;

import cn.zhuhongqing.excel.exception.ExcelConvertException;
import cn.zhuhongqing.excel.utils.GenericUtils;

public class DateConvert extends AbstractExcelConvert {

	@Override
	public boolean isTrueType(Object value) {
		if (value instanceof Date) {
			return true;
		}
		if (value instanceof String) {
			Object returnData = GenericUtils.convertDate(value.toString(),
					GenericUtils.EXCEL_DATE_FORMATTER);
			if (returnData instanceof Date) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int javaToExcelType() {
		return Cell.CELL_TYPE_NUMERIC;
	}

	@Override
	public Object excelToJava(Object value) throws ExcelConvertException {
		if (isTrueType(value)) {
			if (value instanceof Date) {
				return value;
			}
			if (value instanceof String) {
				Object returnData = GenericUtils.convertDate(value.toString(),
						GenericUtils.EXCEL_DATE_FORMATTER);
				if (returnData instanceof Date) {
					return returnData;
				}
			}
		}
		throw new ExcelConvertException("日期类型数据转换失败");
	}

	@Override
	public String excelFormatter() {
		return ExcelConvert.DATE_FORMATTER;
	}

}
