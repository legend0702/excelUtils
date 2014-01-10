package cn.zhuhongqing.excel.convert;

import java.io.File;

import cn.zhuhongqing.excel.ExcelReader;
import cn.zhuhongqing.excel.cell.Row;
import cn.zhuhongqing.excel.cell.Workbook;
import cn.zhuhongqing.excel.cell.Worksheet;
import cn.zhuhongqing.excel.data.DataModel;
import cn.zhuhongqing.excel.data.DataRow;
import cn.zhuhongqing.excel.data.IDataModel;
import cn.zhuhongqing.excel.data.IDataRow;

public class ExcelDataQuerier {

	public IDataModel query(File file, String[] columnNames) {
		Workbook wookbook = ExcelReader.readQuietly(file);
		return query(wookbook, columnNames, 0, 0, 0);
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
