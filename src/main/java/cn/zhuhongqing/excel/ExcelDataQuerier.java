package cn.zhuhongqing.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.zhuhongqing.excel.cell.Cell;
import cn.zhuhongqing.excel.cell.Row;
import cn.zhuhongqing.excel.cell.Workbook;
import cn.zhuhongqing.excel.cell.Worksheet;
import cn.zhuhongqing.excel.data.DataModel;
import cn.zhuhongqing.excel.data.DataRow;
import cn.zhuhongqing.excel.data.IDataModel;
import cn.zhuhongqing.excel.data.IDataRow;

public class ExcelDataQuerier {

	/**
	 * 返回List<Map<Key,Cell>>
	 * 
	 * @param file
	 *            excelFile
	 * @param comparMap
	 *            Key列头
	 * @return
	 */

	public static List<Map<String, Cell>> getCellList(File file,
			Map<String, String> comparMap) {

		return getExcelList(file, comparMap, Cell.class);
	}

	public static List<Map<String, Object>> getValueList(File file,
			Map<String, String> comparMap) {
		return getExcelList(file, comparMap, Object.class);
	}

	@SuppressWarnings("unchecked")
	private static <T> List<Map<String, T>> getExcelList(File file,
			Map<String, String> comparMap, Class<T> returnType) {
		// 创建excel
		Workbook wookbook = ExcelReader.readQuietly(file);
		// 返回的数据
		// key为comparMap的value 值为comparMap的key对应的cell
		List<Map<String, T>> cellList = new ArrayList<Map<String, T>>();
		// 默认第一个sheet
		Worksheet firstSheet = wookbook.getSheet(0);
		// 默认第一列为属性名头列
		Row nameRow = firstSheet.getRow(0);
		// 存放序列跟对应comparMap的value
		Map<Integer, String> indexMap = getIndexMap(nameRow, comparMap);
		// 由于第一列默认为名称列
		// 故从第二列开始才是数据
		for (int i = 1; i < firstSheet.getRowCount(); i++) {
			Row row = firstSheet.getRow(i);
			Map<String, T> cellMap = new HashMap<String, T>();
			Iterator<Entry<Integer, String>> indexItr = indexMap.entrySet()
					.iterator();
			// 迭代序列map,将需要的cell取出来存入cellMap
			while (indexItr.hasNext()) {
				Entry<Integer, String> indexEntry = indexItr.next();
				Integer cellIndex = indexEntry.getKey();
				if (cellIndex < row.getCount()) {
					Cell poiCell = row.getCell(cellIndex);
					// 如果returnType是Cell类型
					if (returnType.equals(Cell.class)) {
						cellMap.put(indexEntry.getValue(), (T) poiCell);
					} else {
						// 不然把值放入
						cellMap.put(indexEntry.getValue(),
								(T) poiCell.getValue());
					}
				}
			}
			cellList.add(cellMap);
		}
		return cellList;
	}

	/**
	 * 创建存放序列跟对应comparMap#value的IndexMap
	 * 
	 * @param nameRow
	 * @param comparMap
	 * @return
	 */

	private static Map<Integer, String> getIndexMap(Row nameRow,
			Map<String, String> comparMap) {
		// 存放序列跟对应comparMap的value
		Map<Integer, String> indexMap = new HashMap<Integer, String>();
		// 默认第一个sheet
		for (int i = 0; i < nameRow.getCount(); i++) {
			// 匹配comparMap中的key 将colum列序跟value存入indexMap中
			String name = (String) nameRow.getCell(i).getValue();
			if (name != null) {
				String propName = comparMap.get(name);
				if (propName != null) {
					indexMap.put(i, propName);
				}
			}
		}
		return indexMap;
	}

	public IDataModel query(File file, String[] columnNames, int sheetIndex,
			int fromRowNo, int fromColumnNo) {
		Workbook wookbook = ExcelReader.readQuietly(file);
		return query(wookbook, columnNames, sheetIndex, fromRowNo, fromColumnNo);
	}

	public IDataModel query(Workbook wookbook, String[] columnNames,
			int sheetIndex, int fromRowNo, int fromColumnNo) {
		int sheetCount = wookbook.getSheetCount();
		if (sheetIndex < sheetCount) {
			Worksheet worksheet = wookbook.getSheet(sheetIndex);
			return query(worksheet, columnNames, fromRowNo, fromColumnNo);
		}
		return new DataModel();
	}

	public IDataModel query(Worksheet worksheet, String[] columnNames,
			int fromRowNo, int fromColumnNo) {
		DataModel dataModel = new DataModel();
		int rowCount = worksheet.getRowCount();
		for (int i = fromRowNo; i < rowCount; i++) {
			Row row = worksheet.getRow(i);
			IDataRow dataRow = read(row, columnNames, fromColumnNo);
			if (dataRow != null) {
				dataModel.addDataRow(dataRow);
			}
		}
		return dataModel;
	}

	private IDataRow read(Row row, String[] columnNames, int fromColumnNo) {
		int cellCount = row.getCount();
		if (fromColumnNo + columnNames.length <= cellCount) {
			DataRow dataRow = new DataRow();
			for (int i = 0; i < columnNames.length; i++) {
				Object cellValue = row.getCell(i + fromColumnNo).getValue();
				dataRow.setData(columnNames[i], cellValue);
			}
			return dataRow;
		}
		return null;
	}
}
