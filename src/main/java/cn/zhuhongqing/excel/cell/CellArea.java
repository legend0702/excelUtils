package cn.zhuhongqing.excel.cell;

public class CellArea {
	private int StartRow = -1;
	private int EndRow = -1;
	private int StartColumn = -1;
	private int EndColumn = -1;
	/**
	 * 取得startRow的值
	 *
	 * @return 返回startRow的值
	 */
	public int getStartRow() {
		return StartRow;
	}
	/**
	 * 设置startRow的值
	 *
	 * @param startRow startRow的新值
	 */
	public void setStartRow(int startRow) {
		StartRow = startRow;
	}
	/**
	 * 取得endRow的值
	 *
	 * @return 返回endRow的值
	 */
	public int getEndRow() {
		return EndRow;
	}
	/**
	 * 设置endRow的值
	 *
	 * @param endRow endRow的新值
	 */
	public void setEndRow(int endRow) {
		EndRow = endRow;
	}
	/**
	 * 取得startColumn的值
	 *
	 * @return 返回startColumn的值
	 */
	public int getStartColumn() {
		return StartColumn;
	}
	/**
	 * 设置startColumn的值
	 *
	 * @param startColumn startColumn的新值
	 */
	public void setStartColumn(int startColumn) {
		StartColumn = startColumn;
	}
	/**
	 * 取得endColumn的值
	 *
	 * @return 返回endColumn的值
	 */
	public int getEndColumn() {
		return EndColumn;
	}
	/**
	 * 设置endColumn的值
	 *
	 * @param endColumn endColumn的新值
	 */
	public void setEndColumn(int endColumn) {
		EndColumn = endColumn;
	}
}
