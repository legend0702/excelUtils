package cn.zhuhongqing.excel.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class DataModel implements IDataModel {
	private List<String> columnNames = null;
	private List<IDataRow> dataRows = null;

	public DataModel() {
		dataRows = new ArrayList<IDataRow>();
		columnNames = new ArrayList<String>();
	}

	@Override
	public int getRowCount() {
		return dataRows.size();
	}

	public void addColumnName(String columnName) {
		columnNames.add(columnName);
	}

	@Override
	public String getColumnName(int columnNo) {
		return columnNames.get(columnNo);
	}

	@Override
	public int getColumnCount() {
		if (dataRows.size() > 0) {
			return dataRows.get(0).getColumnCount();
		}
		return 0;
	}

	@Override
	public Object getValue(int rowNo, String nameField) {
		IDataRow dr = dataRows.get(rowNo);
		return dr.getObject(nameField);
	}

	@Override
	public String getString(int rowNo, String nameField) {
		IDataRow dataRow = dataRows.get(rowNo);
		return dataRow.getString(nameField);
	}

	public void addDataRow(IDataRow dataRow) {
		this.dataRows.add(dataRow);
	}

	@Override
	public IDataRow getDataRow(int rowNo) {
		return dataRows.get(rowNo);
	}

	@Override
	public Date getDate(int rowNo, String nameField) {
		IDataRow dr = dataRows.get(rowNo);
		return dr.getDate(nameField);
	}

	@Override
	public Number getNumber(int rowNo, String nameField) {
		IDataRow dr = dataRows.get(rowNo);
		return dr.getNumber(nameField);
	}

	@Override
	public Collection<IDataRow> getDateRows() {
		return dataRows;
	}
}
