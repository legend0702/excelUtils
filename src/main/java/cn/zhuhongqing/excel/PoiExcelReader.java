package cn.zhuhongqing.excel;

import java.io.InputStream;

import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.zhuhongqing.excel.cell.Workbook;

public class PoiExcelReader {
	public static Workbook read(InputStream input) throws ExcelReaderException {
		try {
			org.apache.poi.ss.usermodel.Workbook poiWorkbook = WorkbookFactory
					.create(input);
			if (poiWorkbook instanceof XSSFWorkbook) {
				return new XSSFExcelReader().read((XSSFWorkbook) poiWorkbook);
			}
			return null;
		} catch (Exception e) {
			throw new ExcelReaderException(e);
		}
	}
}
