package cn.zhuhongqing.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.zhuhongqing.excel.cell.Cell;
import cn.zhuhongqing.excel.convert.ExcelConvertException;
import cn.zhuhongqing.excel.exception.ExcelRuntimeException;
import cn.zhuhongqing.excel.exception.ExcelShortCircuit;
import cn.zhuhongqing.excel.exception.ShortCircuit;
import cn.zhuhongqing.excel.utils.GenericUtils;
import cn.zhuhongqing.excel.utils.StringUtils;

public class ExcelOperationCenter {

	/**
	 * 对数据进行强制转换
	 * 
	 * 如果有类型转换失败则直接抛出异常
	 * 
	 * @param excelFile
	 * @param comparMap
	 * @return
	 * @throws ExcelConvertException
	 */

	public static ExcelData resolveAndConvertExcelData(File excelFile,
			Map<String, String> comparMap) throws ExcelConvertException {

		// 不会抛出ShortCircuit
		try {
			return resolveAndConvertAndCheckExcelData(excelFile, comparMap,
					ExcelOperationType.ConvertThrow);
		} catch (ShortCircuit e) {
			throw new ExcelConvertException(e);
		}
	}

	/**
	 * 对数据进行转换、格式校验
	 * 
	 * 根据异常配置是否立刻抛出异常
	 * 
	 * 如果不抛出,增跟数据一起封装成ExcelData返回
	 * 
	 * 解析失败的数据不会录入到返回的数据中
	 * 
	 * @param excelFile
	 * @param comparMap
	 * @return
	 * @throws ShortCircuit
	 */

	public static ExcelData resolveAndConvertAndCheckExcelData(File excelFile,
			Map<String, String> comparMap) throws ShortCircuit {

		// 不会抛出ExcelConvertException
		try {
			return resolveAndConvertAndCheckExcelData(excelFile, comparMap,
					ExcelOperationType.ConvertCatch);
		} catch (ExcelConvertException e) {
			throw new ShortCircuit(e);
		}
	}

	private static ExcelData resolveAndConvertAndCheckExcelData(File excelFile,
			Map<String, String> comparMap, ExcelOperationType excelOperationType)
			throws ShortCircuit, ExcelConvertException {

		ExcelData excelData = new ExcelData();
		ExcelConvertCenter excelConvertCenter = initExcelConvertCenter(comparMap);
		ExcelShortCircuit excelShortCircuit = initShortCircuit(comparMap);

		List<Map<String, Cell>> cellList = ExcelDataQuerier.getCellList(
				excelFile, comparMap);

		List<Map<String, Object>> colList = new ArrayList<Map<String, Object>>();

		Iterator<Map<String, Cell>> cellItr = cellList.iterator();
		while (cellItr.hasNext()) {
			Map<String, Cell> cellMap = cellItr.next();
			if (cellMap.isEmpty()) {
				continue;
			}
			Map<String, Object> colMap = new HashMap<String, Object>();
			Iterator<Entry<String, Cell>> cellMapItr = cellMap.entrySet()
					.iterator();
			while (cellMapItr.hasNext()) {
				Entry<String, Cell> cellEntry = cellMapItr.next();
				String cellName = cellEntry.getKey();
				Cell entryCell = cellEntry.getValue();
				Object orginCellValue = entryCell.getValue();
				if (orginCellValue != null && orginCellValue instanceof String) {
					orginCellValue = ((String) orginCellValue).trim();
				}
				if (StringUtils.isNull(orginCellValue)) {
					continue;
				}
				try {
					colMap.put(GenericUtils.splitFirstUpSubLine(cellName, "_"),
							excelConvertCenter.excelToJava(cellName,
									orginCellValue));
				} catch (ExcelConvertException e) {
					switch (excelOperationType) {
					case ConvertThrow:
						// ExcelConvertException excelRuntimeException = null;
						// String excelColName = (String)
						// GenericUtils.mapValueGetKey(
						// prop, cellName);
						// excelRuntimeException = new
						// ExcelRuntimeException("第["
						// + (entryCell.getRowNo() + 1)
						// + "]行["
						// + (excelColName == null ? (entryCell
						// .getColumnNo() + 1) : excelColName)
						// + "]" + e.getMessage());
						e = new ExcelConvertException(
								ExcelUtils.IntdexToStr(entryCell.getColumnNo() + 1)
										+ (entryCell.getRowNo() + 1)
										+ "单元格"
										+ e.getMessage());
						throw e;
					case ConvertCatch:
						ExcelRuntimeException excelRuntimeException = null;
						// String excelColName = (String)
						// GenericUtils.mapValueGetKey(
						// prop, cellName);
						// excelRuntimeException = new
						// ExcelRuntimeException("第["
						// + (entryCell.getRowNo() + 1)
						// + "]行["
						// + (excelColName == null ? (entryCell
						// .getColumnNo() + 1) : excelColName)
						// + "]" + e.getMessage());
						excelRuntimeException = new ExcelRuntimeException(
								ExcelUtils.IntdexToStr(entryCell.getColumnNo() + 1)
										+ (entryCell.getRowNo() + 1)
										+ "单元格"
										+ e.getMessage());
						excelShortCircuit.addException(cellName,
								excelRuntimeException);
						break;
					default:
						break;
					}
				}
			}
			// 过滤空Map
			if (colMap.isEmpty()) {
				continue;
			}
			colList.add(colMap);
		}
		excelData.setExcelData(colList);
		excelData.setExcelShortCircuit(excelShortCircuit);
		return excelData;
	}

	/**
	 * 直接获得数据
	 * 
	 * 不检测类型
	 * 
	 * 不做类型强制转换
	 * 
	 * 没有异常机制
	 * 
	 * @param excelFile
	 *            excel文件
	 * @param comparMap
	 *            头文件对应
	 * @return
	 */
	public static ExcelData resolveExcelData(File excelFile,
			Map<String, String> comparMap) {
		ExcelData excelData = new ExcelData();
		excelData.setExcelData(ExcelDataQuerier.getValueList(excelFile,
				comparMap));
		return excelData;
	}

	// 初始化短路异常机制
	private static ExcelShortCircuit initShortCircuit(
			Map<String, String> comparMap) {
		// 异常管理机制
		ExcelShortCircuit shortCircuit = new ExcelShortCircuit();
		shortCircuit.initSetting(comparMap);
		return shortCircuit;
	}

	// 获得类型转换器
	private static ExcelConvertCenter initExcelConvertCenter(
			Map<String, String> comparMap) {
		return ExcelConvertCenter.createExcelOperationCenter(comparMap);
	}

	/**
	 * 数据在{@link ExcelData#getExcelData()}
	 * 
	 * 异常在{@link ExcelData#getExcelShortCircuit()}
	 * 
	 * @author zhq mail:qwepoidjdj(a)hotmail.com
	 * 
	 */

	static class ExcelData {
		private List<Map<String, Object>> excelData = new ArrayList<Map<String, Object>>(
				0);

		private ExcelShortCircuit excelShortCircuit = new ExcelShortCircuit();

		public List<Map<String, Object>> getExcelData() {
			return excelData;
		}

		protected void setExcelData(List<Map<String, Object>> excelData) {
			this.excelData = excelData;
		}

		public ExcelShortCircuit getExcelShortCircuit() {
			return excelShortCircuit;
		}

		protected void setExcelShortCircuit(ExcelShortCircuit excelShortCircuit) {
			this.excelShortCircuit = excelShortCircuit;
		}
	}

	private enum ExcelOperationType {
		ConvertThrow, ConvertCatch;
	}
}
