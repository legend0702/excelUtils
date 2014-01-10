package cn.zhuhongqing.excel;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import cn.zhuhongqing.excel.cell.Workbook;
import cn.zhuhongqing.excel.exception.ExcelConvertException;
import cn.zhuhongqing.excel.exception.ExcelShortCircuit;
import cn.zhuhongqing.excel.utils.GenericUtils;

public class ExcelTest {

	@Test
	public void test1() throws Exception {
		String filePath = "D:/2013-12-28.xlsx";
		File excelFile = new File(filePath);
		System.out.println(excelFile.getName());
		FileInputStream input = new FileInputStream(excelFile);
		Workbook workbook = PoiExcelReader.read(input);
		System.out.println(workbook.getWorksheets().get(0).getCells());
		input.close();
		System.out.println("OK");
	}

	@Test
	public void test2() throws Exception {

		Map<Class<?>, Object> constructorTypeAndParam = new HashMap<Class<?>, Object>();

		constructorTypeAndParam.put(Throwable.class, new RuntimeException());

		constructorTypeAndParam.put(String.class, "出错拉～");

		ExcelConvertException e = GenericUtils.autoCreateObject(
				ExcelConvertException.class, constructorTypeAndParam);

		System.out.println(e.getMessage());
	}

	@Test
	public void test3() throws Exception {
		ExcelShortCircuit excelSC = new ExcelShortCircuit();
		System.out.println(excelSC);
	}
}
