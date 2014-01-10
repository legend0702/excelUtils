package cn.zhuhongqing.excel.cell;

import java.util.ArrayList;
import java.util.List;

public class Row {
    private int height = 0;
    private List<Cell> cells = null;

    public Row() {
        cells = new ArrayList<Cell>();
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

    /**
     * 增加单元格对象
     * 
     * @param cell
     */
    public void addCell(Cell cell) {
        cells.add(cell);
    }

    /**
     * 取得有效单元格的数量
     * 
     * @return 有效单元格的数量
     */
    public int getCount() {
        return cells.size();
    }

    public Cell getCell(int index) {
        return cells.get(index);
    }
}
