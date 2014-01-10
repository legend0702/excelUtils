package cn.zhuhongqing.excel.cell;

import java.util.ArrayList;
import java.util.List;

public class WorksheetCollection {
    private List<Worksheet> worksheets = null;

    public WorksheetCollection() {
        worksheets = new ArrayList<Worksheet>();
    }

    public void addWorksheet(Worksheet worksheet) {
        worksheets.add(worksheet);
    }

    public int getCount() {
        return worksheets.size();
    }

    public Worksheet get(int index) {
        return worksheets.get(index);
    }
}
