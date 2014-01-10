package cn.zhuhongqing.excel.cell;

public class Worksheet {
    private String name = null;
    private Cells cells = null;

    /**
     * 取得name的值
     * 
     * @return 返回name的值
     */
    public String getName() {
        return name;
    }

    /**
     * 设置name的值
     * 
     * @param name
     *            name的新值
     */
    public void setName(String name) {
        this.name = name;
    }

    public int getRowCount() {
        return cells.getRowCollection().getCount();
    }

    public Row getRow(int index) {
        return cells.getRowCollection().getRow(index);
    }

    /**
     * 取得cells的值
     * 
     * @return 返回cells的值
     */
    public Cells getCells() {
        return cells;
    }

    /**
     * 设置cells的值
     * 
     * @param cells
     *            cells的新值
     */
    public void setCells(Cells cells) {
        this.cells = cells;
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
        return cells.getCell(rowNo, columnNo);
    }
}
