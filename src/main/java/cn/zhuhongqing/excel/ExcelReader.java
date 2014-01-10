package cn.zhuhongqing.excel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import cn.zhuhongqing.excel.cell.Workbook;

public class ExcelReader {
	public static Workbook read(byte[] excelFile) throws ExcelReaderException {
		return read(new ByteArrayInputStream(excelFile));
	}

	public static Workbook read(File file) throws ExcelReaderException {
		InputStream input = null;
		try {
			input = new FileInputStream(file);
			return read(input);
		} catch (IOException e) {
			throw new ExcelReaderException(e);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

	public static Workbook readQuietly(File file) {
		try {
			return read(file);
		} catch (ExcelReaderException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Workbook read(InputStream input) throws ExcelReaderException {
		return PoiExcelReader.read(input);
	}
}
