package cn.zhuhongqing.excel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import cn.zhuhongqing.excel.ExcelOperationCenter.ExcelData;
import cn.zhuhongqing.excel.cell.Cell;
import cn.zhuhongqing.excel.cell.Workbook;
import cn.zhuhongqing.excel.convert.ExcelConvertException;
import cn.zhuhongqing.excel.exception.ExcelRuntimeException;
import cn.zhuhongqing.excel.exception.ExcelShortCircuit;
import cn.zhuhongqing.excel.exception.ShortCircuit;
import cn.zhuhongqing.excel.utils.GenericUtils;
import cn.zhuhongqing.excel.utils.StringUtils;

public class ExcelTest {

	File excelFile = null;

	Workbook workbook = null;

	Map<String, String> prop = null;

	@Before
	public void workBookInit() throws ExcelReaderException, IOException {
		String filePath = "D:/2013-12-28.xlsx";
		excelFile = new File(filePath);
		System.out.println(excelFile.getName());
		// FileInputStream input = new FileInputStream(excelFile);
		// workbook = PoiExcelReader.read(input);
		// input.close();
		// System.out.println("workbook Init!workbook.sheetCount:["
		// + workbook.getSheetCount() + "]");
		prop = GenericUtils
				.loadProp("cn/zhuhongqing/excel/TestImport.properties");
		System.out.println("prop Init!prop.size:[" + prop.size() + "]");
	}

	@Test
	public void test1() throws ShortCircuit {
		System.out.println("Test Start!");

		ExcelConvertCenter excelOperationCenter = ExcelConvertCenter
				.createExcelOperationCenter(prop);

		ExcelShortCircuit excelSC = new ExcelShortCircuit();
		excelSC.initSetting(prop.get(ExcelShortCircuit.SHORTCIRCUIT_KEY));

		List<Map<String, Cell>> cellList = ExcelDataQuerier.getCellList(
				excelFile, prop);

		List<Map<String, Object>> colList = new ArrayList<Map<String, Object>>();

		Iterator<Map<String, Cell>> cellItr = cellList.iterator();
		while (cellItr.hasNext()) {
			Map<String, Object> colMap = new HashMap<String, Object>();
			Map<String, Cell> cellMap = cellItr.next();
			Iterator<Entry<String, Cell>> cellMapItr = cellMap.entrySet()
					.iterator();
			while (cellMapItr.hasNext()) {
				Entry<String, Cell> cellEntry = cellMapItr.next();
				String cellName = cellEntry.getKey();
				Cell entryCell = cellEntry.getValue();
				if (StringUtils.isNull(entryCell.getValue())) {
					continue;
				}
				try {
					colMap.put(cellName, excelOperationCenter.excelToJava(
							cellName, entryCell.getValue()));
				} catch (ExcelConvertException e) {
					ExcelRuntimeException excelRuntimeException = null;
					// String excelColName = (String)
					// GenericUtils.mapValueGetKey(
					// prop, cellName);
					// excelRuntimeException = new ExcelRuntimeException("第["
					// + (entryCell.getRowNo() + 1)
					// + "]行["
					// + (excelColName == null ? (entryCell
					// .getColumnNo() + 1) : excelColName)
					// + "]" + e.getMessage());
					excelRuntimeException = new ExcelRuntimeException(
							ExcelUtils.IntdexToStr(entryCell.getColumnNo() + 1)
									+ (entryCell.getRowNo() + 1) + "单元格"
									+ e.getMessage());
					excelSC.addException(cellName, excelRuntimeException);
				}
			}
			colList.add(colMap);
		}
		System.out.println(excelSC.getExceptionMessage());
		System.out.println(colList.size());
		System.out.println("Test OK!");
	}

	@Test
	public void test2() throws ShortCircuit, ExcelConvertException {
		// ExcelData excelData = ExcelOperationCenter
		// .resolveAndConvertAndCheckExcelData(excelFile, prop);

		ExcelData excelData = ExcelOperationCenter.resolveAndConvertExcelData(
				excelFile, prop);

		// System.out.println(excelData.getExcelData().size());
		// System.out.println(excelData.getExcelShortCircuit()
		// .getExceptionMessage());

	}
}
