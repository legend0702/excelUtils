package cn.zhuhongqing.excel;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import cn.zhuhongqing.excel.cell.Workbook;
import cn.zhuhongqing.excel.exception.ExcelConvertException;
import cn.zhuhongqing.excel.exception.ShortCircuit;
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
		NullPointerException nullPointerException = new NullPointerException(
				"空指针");
		ExcelConvertException excelConvertException = new ExcelConvertException(
				"类型异常");
		ShortCircuit shortCircuit = new ShortCircuit();
		shortCircuit.setShortCircuit(false);
		shortCircuit.setShortSize(2);

		shortCircuit.addException(nullPointerException);
		shortCircuit.addException(excelConvertException);

		shortCircuit.addException("转换异常");

		// shortCircuit.getCause().printStackTrace();
		// shortCircuit.fillInStackTrace().printStackTrace();
		// System.out.println(shortCircuit.getStackTrace().length);
	}

	@Test
	public void test4() throws Exception {
		NullPointerException nullPointerException = new NullPointerException(
				"空指针");

		Constructor<ShortCircuit> constructor = ShortCircuit.class
				.getConstructor(Throwable.class);

		ShortCircuit shortCircuit = constructor
				.newInstance(nullPointerException);

		System.out.println(shortCircuit);
	}

	@Test
	public void test5() throws ShortCircuit {

		NullPointerException nullPointerException = new NullPointerException(
				"空指针");

		ShortCircuit shortCircuit = new ShortCircuit();

		shortCircuit.addException(nullPointerException);

		// System.out.println(shortCircuit.getExceptionMessage());

	}

	@Test
	public void test6() throws ShortCircuit {
		NullPointerException nullPointerException = new NullPointerException(
				"空指针");

		ShortCircuit s1 = new ShortCircuit("异常1", nullPointerException);
		ShortCircuit s2 = new ShortCircuit("异常2", s1);
		ShortCircuit s3 = new ShortCircuit("异常3", s2);

		throw s3;
	}

	@Test
	public void test7() throws ShortCircuit {
		ShortCircuit shortCircuit = new ShortCircuit();

		NullPointerException nullPointerException = new NullPointerException(
				"空指针");

		ExcelConvertException dateException = new ExcelConvertException(
				"日期类型异常");

		ExcelConvertException numberException = new ExcelConvertException(
				"数字类型异常");

		ShortCircuit s1 = new ShortCircuit("异常1", nullPointerException);
		ShortCircuit s2 = new ShortCircuit(s1.getMessage(), s1.getCause());
		ShortCircuit s3 = new ShortCircuit(s2.getMessage(), numberException);

		// shortCircuit.addSuppressed(nullPointerException);
		// shortCircuit.addSuppressed(dateException);
		// shortCircuit.addSuppressed(numberException);

		throw s3;
	}
}
