package cn.zhuhongqing.excel.cell;

import java.util.ArrayList;
import java.util.List;

public class Cells {
    private int[] columnWidths = null;

    private RowCollection rowCollection = null;

    private List<CellArea> mergeCells = null;

    public Cells() {
        mergeCells = new ArrayList<CellArea>();
    }

    /**
     * 取得指定列的宽度值
     * 
     * @param columnNo
     *            列号
     * @return 宽度值
     */
    public int getColumnWidth(int columnNo) {
        return columnWidths[columnNo];
    }

    /**
     * 取得columnWidths的值
     * 
     * @return 返回columnWidths的值
     */
    public int[] getColumnWidths() {
        return columnWidths;
    }

    /**
     * 设置columnWidths的值
     * 
     * @param columnWidths
     *            columnWidths的新值
     */
    public void setColumnWidths(int[] columnWidths) {
        this.columnWidths = columnWidths;
    }

    /**
     * 取得rowCollection的值
     * 
     * @return 返回rowCollection的值
     */
    public RowCollection getRowCollection() {
        return rowCollection;
    }

    /**
     * 设置rowCollection的值
     * 
     * @param rowCollection
     *            rowCollection的新值
     */
    public void setRowCollection(RowCollection rowCollection) {
        this.rowCollection = rowCollection;
    }

    /**
     * 取得指定行和指定列的单元格
     * 
     * @param rowNo
     *            指定的行号
     * @param columnNo
     *            指定的列号
     * @return 单元格对象
     */
    public Cell getCell(int rowNo, int columnNo) {
        return rowCollection.getCell(rowNo, columnNo);
    }

    public List<CellArea> getMergedCells() {
        return mergeCells;
    }

    public void addMergedCell(CellArea cellArea) {
        mergeCells.add(cellArea);
    }
}
