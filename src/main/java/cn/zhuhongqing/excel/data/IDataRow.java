package cn.zhuhongqing.excel.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public interface IDataRow {
	public String getDataColumn(String columnName);

	public Date getDate(String columnName);

	public String getString(String columnName);

	public Object getObject(String columnName);

	public BigDecimal getBigDecimal(String columnName);

	public Integer getInteger(String columnName);

	public Number getNumber(String columnName);

	public int getColumnCount();

	public Map<String, Object> getValueMap();

	public void setValueMap(Map<String, Object> valueMap);

	public void setData(String columnName, Object value);

	public Object removeData(String columnName);
}
