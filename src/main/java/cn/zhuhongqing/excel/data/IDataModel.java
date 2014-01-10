package cn.zhuhongqing.excel.data;

import java.util.Collection;
import java.util.Date;

public interface IDataModel {
	public int getRowCount();

	public int getColumnCount();

	public String getColumnName(int columnNo);

	public Object getValue(int rowNo, String nameField);

	public String getString(int rowNo, String nameField);

	public Date getDate(int rowNo, String nameField);

	public Number getNumber(int rowNo, String nameField);

	public IDataRow getDataRow(int rowNo);

	public Collection<IDataRow> getDateRows();

	public void addDataRow(IDataRow dataRow);
}
