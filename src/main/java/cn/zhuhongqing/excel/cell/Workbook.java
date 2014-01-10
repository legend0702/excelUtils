package cn.zhuhongqing.excel.cell;

public class Workbook {
    private StyleCollection styleCollection = null;
    private WorksheetCollection worksheets = null;

    /**
     * 取得styleCollection的值
     * 
     * @return 返回styleCollection的值
     */
    public StyleCollection getStyleCollection() {
        return styleCollection;
    }

    /**
     * 设置styleCollection的值
     * 
     * @param styleCollection
     *            styleCollection的新值
     */
    public void setStyleCollection(StyleCollection styleCollection) {
        this.styleCollection = styleCollection;
    }

    /**
     * 取得defaultStyle的值
     * 
     * @return 返回defaultStyle的值
     */
    public Style getDefaultStyle() {
        return styleCollection.getDefaultStyle();
    }

    /**
     * 设置defaultStyle的值
     * 
     * @param defaultStyle
     *            defaultStyle的新值
     */
    public void setDefaultStyle(Style defaultStyle) {
        styleCollection.setDefaultStyle(defaultStyle);
    }

    /**
     * 取得worksheets的值
     * 
     * @return 返回worksheets的值
     */
    public WorksheetCollection getWorksheets() {
        return worksheets;
    }

    /**
     * 设置worksheets的值
     * 
     * @param worksheets
     *            worksheets的新值
     */
    public void setWorksheets(WorksheetCollection worksheets) {
        this.worksheets = worksheets;
    }

    /**
     * 取得指定名称sheet页的编号
     * 
     * @param sheetName
     *            sheet名称
     * @return sheet页的编号
     */
    public int getSheetIndex(String sheetName) {
        int sheetCount = worksheets.getCount();
        for (int i = 0; i < sheetCount; i++) {
            if (worksheets.get(i).getName().equals(sheetName)) {
                return i;
            }
        }
        return -1;
    }

    public int getSheetCount() {
        return worksheets.getCount();
    }

    /**
     * 取得指定名称sheet页的编号
     * 
     * @param sheetName
     *            sheet名称
     * @return sheet页的编号
     */
    public Worksheet getSheet(String sheetName) {
        int sheetCount = worksheets.getCount();
        for (int i = 0; i < sheetCount; i++) {
            Worksheet worksheet = worksheets.get(i);
            if (worksheet.getName().equals(sheetName)) {
                return worksheet;
            }
        }
        return null;
    }

    public Worksheet getSheet(int sheetIndex) {
        return worksheets.get(sheetIndex);
    }
}
