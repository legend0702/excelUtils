package cn.zhuhongqing.excel.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DataRow implements IDataRow {
	private Map<String, Object> valueMap;

	public DataRow() {
		valueMap = new HashMap<String, Object>();
	}

	@Override
	public String getDataColumn(String columnName) {
		return (String) valueMap.get(columnName);
	}

	public int getColumnCount() {
		return valueMap.size();
	}

	public void setData(String columnName, Object value) {
		valueMap.put(columnName, value);
	}

	public Map<String, Object> getValueMap() {
		return valueMap;
	}

	public void setValueMap(Map<String, Object> valueMap) {
		this.valueMap = valueMap;
	}

	@Override
	public Date getDate(String columnName) {
		return (Date) valueMap.get(columnName);
	}

	@Override
	public BigDecimal getBigDecimal(String columnName) {
		return (BigDecimal) valueMap.get(columnName);
	}

	@Override
	public Integer getInteger(String columnName) {
		return (Integer) valueMap.get(columnName);
	}

	@Override
	public String getString(String columnName) {
		return (String) valueMap.get(columnName);
	}

	@Override
	public Object getObject(String columnName) {
		return valueMap.get(columnName);
	}

	@Override
	public Object removeData(String columnName) {
		return valueMap.remove(columnName);
	}

	@Override
	public Number getNumber(String columnName) {
		return (Number) valueMap.get(columnName);
	}
}
