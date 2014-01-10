package cn.zhuhongqing.excel.cell;

import java.util.ArrayList;
import java.util.List;

public class RowCollection {
    private List<Row> rows = null;

    public RowCollection() {
        rows = new ArrayList<Row>();
    }

    /**
     * 取得count的值
     * 
     * @return 返回count的值
     */
    public int getCount() {
        return rows.size();
    }

    public void addRow(Row row) {
        rows.add(row);
    }

    public Row getRow(int index) {
        return rows.get(index);
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
        return rows.get(rowNo).getCell(columnNo);
    }
}
