package cn.zhuhongqing.excel.cell;

import org.apache.commons.lang3.StringUtils;

public class Cell {
	private int columnNo = 0;
	private int rowNo = 0;
	private int type = 0;
	private Object value = null;
	private int width = 0;
	private int height = 0;
	private Style style = null;

	/**
	 * 取得columnNo的值
	 * 
	 * @return 返回columnNo的值
	 */
	public int getColumnNo() {
		return columnNo;
	}

	/**
	 * 设置columnNo的值
	 * 
	 * @param columnNo
	 *            columnNo的新值
	 */
	public void setColumnNo(int columnNo) {
		this.columnNo = columnNo;
	}

	/**
	 * 取得rowNo的值
	 * 
	 * @return 返回rowNo的值
	 */
	public int getRowNo() {
		return rowNo;
	}

	/**
	 * 设置rowNo的值
	 * 
	 * @param rowNo
	 *            rowNo的新值
	 */
	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}

	/**
	 * 取得type的值
	 * 
	 * @return 返回type的值
	 */
	public int getType() {
		return type;
	}

	/**
	 * 设置type的值
	 * 
	 * @param type
	 *            type的新值
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 取得value的值
	 * 
	 * @return 返回value的值
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * 设置value的值
	 * 
	 * @param value
	 *            value的新值
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * 取得width的值
	 * 
	 * @return 返回width的值
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * 设置width的值
	 * 
	 * @param width
	 *            width的新值
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * 取得height的值
	 * 
	 * @return 返回height的值
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * 设置height的值
	 * 
	 * @param height
	 *            height的新值
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	public Style getStyle() {
		return style;
	}

	public void setStyle(Style style) {
		this.style = style;
	}

	public String getStringValue() {
		if (value == null) {
			return StringUtils.EMPTY;
		}
		return String.valueOf(value);
	}

	public String getFormatValue() {
		return getStringValue();
	}
}
