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

/**
 * 主要进行excel操作
 * 
 * 也是对外主要开放的方法
 * 
 * 包含异常处理,类型转换处理等
 * 
 * @author zhq mail:qwepoidjdj(a)hotmail.com
 * @since 1.6
 * 
 */

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
	 * @throws ExcelReaderException
	 */

	public static ExcelData resolveAndConvertExcelData(File excelFile,
			Map<String, String> comparMap) throws ExcelConvertException,
			ExcelReaderException {

		// 不会抛出ShortCircuitF
		try {
			return resolveAndConvertAndCheckExcelData(excelFile, comparMap,
					true, ExcelOperationType.ConvertThrow);
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
	 * @throws ExcelReaderException
	 */

	public static ExcelData resolveAndConvertAndCheckExcelData(File excelFile,
			Map<String, String> comparMap) throws ShortCircuit,
			ExcelReaderException {

		// 不会抛出ExcelConvertException
		try {
			return resolveAndConvertAndCheckExcelData(excelFile, comparMap,
					true, ExcelOperationType.ConvertCatch);
		} catch (ExcelConvertException e) {
			throw new ShortCircuit(e);
		}
	}

	/**
	 * 将excel数据读出来并转换成List<Map<String,Object>>
	 * 
	 * 并进行一些操作:
	 * 
	 * 底层数据不会过滤空 也不会trim 故这里要做一些处理
	 * 
	 * @param excelFile
	 *            excel文件
	 * @param comparMap
	 *            列名头key匹配
	 * @param checkcompar
	 *            是否检测列名key一致性
	 * @param excelOperationType
	 * 
	 * @return
	 * @throws ShortCircuit
	 * @throws ExcelConvertException
	 * @throws ExcelReaderException
	 */

	private static ExcelData resolveAndConvertAndCheckExcelData(File excelFile,
			Map<String, String> comparMap, boolean checkcompar,
			ExcelOperationType excelOperationType) throws ShortCircuit,
			ExcelConvertException, ExcelReaderException {

		ExcelData excelData = new ExcelData();
		ExcelConvertCenter excelConvertCenter = initExcelConvertCenter(comparMap);
		ExcelShortCircuit excelShortCircuit = initShortCircuit(comparMap);

		List<Map<String, Cell>> cellList = ExcelDataQuerier.getCellList(
				excelFile, comparMap, checkcompar);

		List<Map<String, Object>> colList = new ArrayList<Map<String, Object>>();

		Iterator<Map<String, Cell>> cellItr = cellList.iterator();
		while (cellItr.hasNext()) {
			Map<String, Cell> cellMap = cellItr.next();
			// 过滤空的map
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
					colMap.put(GenericUtils.createPropName(cellName),
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
	 * @throws ExcelReaderException
	 */
	public static ExcelData resolveExcelData(File excelFile,
			Map<String, String> comparMap) throws ExcelReaderException {
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
