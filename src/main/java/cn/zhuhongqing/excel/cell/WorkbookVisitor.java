package cn.zhuhongqing.excel.cell;

public class WorkbookVisitor {
    public void visit(Workbook workbook) {
        WorksheetCollection worksheets = workbook.getWorksheets();
        int sheetCount = worksheets.getCount();
        for (int i = 0; i < sheetCount; i++) {
            visit(worksheets.get(i));
        }
    }

    public void visit(Worksheet worksheet) {
        Cells cells = worksheet.getCells();
        RowCollection rowCollection = cells.getRowCollection();
        int rowCount = rowCollection.getCount();
        for (int i = 0; i < rowCount; i++) {
            visit(rowCollection.getRow(i));
        }
    }

    public void visit(Row row) {
        int cellCount = row.getCount();
        for (int i = 0; i < cellCount; i++) {
            visit(row.getCell(i));
        }
    }

    public void visit(Cell cell) {

    }
}
