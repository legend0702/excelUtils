package cn.zhuhongqing.excel;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.zhuhongqing.excel.cell.Border;
import cn.zhuhongqing.excel.cell.BorderCollection;
import cn.zhuhongqing.excel.cell.BorderLineType;
import cn.zhuhongqing.excel.cell.BorderType;
import cn.zhuhongqing.excel.cell.Cell;
import cn.zhuhongqing.excel.cell.CellArea;
import cn.zhuhongqing.excel.cell.CellValueType;
import cn.zhuhongqing.excel.cell.Cells;
import cn.zhuhongqing.excel.cell.Color;
import cn.zhuhongqing.excel.cell.Font;
import cn.zhuhongqing.excel.cell.Row;
import cn.zhuhongqing.excel.cell.RowCollection;
import cn.zhuhongqing.excel.cell.Style;
import cn.zhuhongqing.excel.cell.StyleCollection;
import cn.zhuhongqing.excel.cell.TextAlignmentType;
import cn.zhuhongqing.excel.cell.Workbook;
import cn.zhuhongqing.excel.cell.Worksheet;
import cn.zhuhongqing.excel.cell.WorksheetCollection;

public class XSSFExcelReader {
	private XSSFWorkbook poiWorkbook = null;
	private XSSFSheet poiWorksheet = null;
	private XSSFRow poiRow = null;
	private XSSFCell poiCell = null;
	private XSSFCellStyle poiStyle = null;

	private Workbook workbook = null;
	private StyleCollection styleCollection = null;
	private WorksheetCollection worksheets = null;
	private Worksheet worksheet = null;
	private Cells cells = null;
	private RowCollection rowCollection = null;
	private Row row = null;
	private Cell cell = null;

	public Workbook read(XSSFWorkbook poiWorkbook) throws ExcelReaderException {
		try {
			this.poiWorkbook = poiWorkbook;
			workbook = new Workbook();
			readWorkbook();
			return workbook;
		} catch (Exception e) {
			throw new ExcelReaderException(e);
		}
	}

	private void readWorkbook() throws ExcelReaderException {
		readStyleCollection();

		Style style = Style.createDefaultStyle();
		styleCollection.setDefaultStyle(style);

		readWorksheets();
	}

	private void readStyleCollection() {
		styleCollection = new StyleCollection();
		workbook.setStyleCollection(styleCollection);
	}

	private void readWorksheets() {
		worksheets = new WorksheetCollection();
		workbook.setWorksheets(worksheets);

		int count = poiWorkbook.getNumberOfSheets();
		for (int i = 0; i < count; i++) {
			poiWorksheet = poiWorkbook.getSheetAt(i);
			readWorksheet();
		}
	}

	private void readWorksheet() {
		worksheet = new Worksheet();
		worksheets.addWorksheet(worksheet);

		worksheet.setName(poiWorksheet.getSheetName());

		cells = new Cells();
		worksheet.setCells(cells);

		int firstRowNum = poiWorksheet.getFirstRowNum();
		int lastRowNum = poiWorksheet.getLastRowNum();

		// 读取列宽信息
		int maxColumnNum = -1;
		for (int i = firstRowNum; i <= lastRowNum; i++) {
			XSSFRow row = poiWorksheet.getRow(i);
			if (row != null) {
				int lastCellNum = row.getLastCellNum();
				if (lastCellNum > maxColumnNum) {
					maxColumnNum = lastCellNum;
				}
			}
		}
		int[] columnWidths = new int[maxColumnNum + 1];
		for (int i = 0; i < maxColumnNum; i++) {
			columnWidths[i] = poiWorksheet.getColumnWidth(i);
			columnWidths[i] = (int) (columnWidths[i]
					* XSSFWorkbook.DEFAULT_CHARACTER_WIDTH / 256);
		}
		cells.setColumnWidths(columnWidths);

		// 读取sheet中的行信息
		rowCollection = new RowCollection();
		cells.setRowCollection(rowCollection);
		for (int i = 0; i < firstRowNum; i++) {
			row = new Row();
			row.setHeight(poiWorksheet.getDefaultRowHeight());
			rowCollection.addRow(row);
		}
		for (int i = firstRowNum; i <= lastRowNum; i++) {
			poiRow = poiWorksheet.getRow(i);
			if (poiRow == null) {
				row = new Row();
				row.setHeight(poiWorksheet.getDefaultRowHeight());
			} else {
				row = readRow(poiRow, i);
			}
			rowCollection.addRow(row);
		}

		// 读取合并单元格信息
		readMergedCells();
	}

	/**
	 * 读取合并单元格信息
	 */
	private void readMergedCells() {
		int numMergedRegions = poiWorksheet.getNumMergedRegions();
		for (int i = 0; i < numMergedRegions; i++) {
			CellRangeAddress poiMergedCell = poiWorksheet.getMergedRegion(i);
			CellArea cellArea = new CellArea();
			cellArea.setStartRow(poiMergedCell.getFirstRow());
			cellArea.setEndRow(poiMergedCell.getLastRow());
			cellArea.setStartColumn(poiMergedCell.getFirstColumn());
			cellArea.setEndColumn(poiMergedCell.getLastColumn());
			cells.addMergedCell(cellArea);
		}
	}

	private Row readRow(XSSFRow poiRow, int rowNo) {
		row = new Row();

		row.setHeight((int) poiRow.getHeightInPoints());
		int firstCellNum = poiRow.getFirstCellNum();
		if (firstCellNum >= 0) {
			for (int i = 0; i < firstCellNum; i++) {
				Cell defaultCell = createDefaultCell(rowNo, i);
				row.addCell(defaultCell);
			}

			int lastCellNum = poiRow.getLastCellNum();
			for (int i = firstCellNum; i < lastCellNum; i++) {
				poiCell = poiRow.getCell(i);
				if (poiCell == null) {
					cell = createDefaultCell(rowNo, i);
				} else {
					cell = readCell(poiCell);
				}
				row.addCell(cell);
			}
		}

		return row;
	}

	private Cell createDefaultCell(int rowNo, int colNo) {
		Cell defaultCell = new Cell();
		defaultCell.setRowNo(rowNo);
		defaultCell.setColumnNo(colNo);
		defaultCell.setStyle(workbook.getDefaultStyle());
		return defaultCell;
	}

	private Cell readCell(XSSFCell poiCell) {
		cell = new Cell();

		int column = poiCell.getColumnIndex();
		cell.setColumnNo(column);
		cell.setRowNo(poiCell.getRowIndex());
		int cellType = poiCell.getCellType();

		switch (cellType) {
		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BLANK: {
			cell.setType(CellValueType.IS_NULL);
			// cell.setValue(StringUtils.EMPTY);
			break;
		}
		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC: {
			cell.setType(CellValueType.IS_NUMERIC);
			if (DateUtil.isCellDateFormatted(poiCell)) {
				cell.setType(CellValueType.IS_DATE_TIME);
				cell.setValue(HSSFDateUtil.getJavaDate(poiCell
						.getNumericCellValue()));
			} else {
				cell.setValue(poiCell.getNumericCellValue());
			}
			break;
		}
		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING: {
			cell.setType(CellValueType.IS_STRING);
			cell.setValue(poiCell.getStringCellValue());
			break;
		}
		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_FORMULA: {
			cell.setType(CellValueType.IS_STRING);
			cell.setValue(poiCell.getCellFormula());
			break;
		}
		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN: {
			cell.setType(CellValueType.IS_BOOL);
			cell.setValue(poiCell.getBooleanCellValue());
			break;
		}
		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_ERROR: {
			cell.setType(CellValueType.IS_ERROR);
			// cell.setValue(StringUtils.EMPTY);
			break;
		}
		}
		cell.setWidth(cells.getColumnWidth(column));
		cell.setHeight(row.getHeight());

		poiStyle = poiCell.getCellStyle();
		Style style = readStyle(poiStyle);
		style = styleCollection.addStyle(style);
		cell.setStyle(style);

		return cell;
	}

	private Style readStyle(XSSFCellStyle poiStyle) {
		Style style = new Style();

		XSSFColor poiBackgroundColor = poiStyle.getFillForegroundColorColor();
		if (poiBackgroundColor != null) {
			style.setBackgroundColor(readColor(poiBackgroundColor));
		}

		// 水平对齐类型
		switch (poiStyle.getAlignment()) {
		case org.apache.poi.ss.usermodel.CellStyle.ALIGN_LEFT: {
			style.setHorizontalAlignment(TextAlignmentType.LEFT);
			break;
		}
		case org.apache.poi.ss.usermodel.CellStyle.ALIGN_RIGHT: {
			style.setHorizontalAlignment(TextAlignmentType.RIGHT);
			break;
		}
		case org.apache.poi.ss.usermodel.CellStyle.ALIGN_GENERAL: {
			style.setHorizontalAlignment(TextAlignmentType.GENERAL);
			break;
		}
		default: {
			style.setHorizontalAlignment(TextAlignmentType.CENTER);
		}
		}

		// 垂直对齐类型
		switch (poiStyle.getVerticalAlignment()) {
		case org.apache.poi.ss.usermodel.CellStyle.VERTICAL_BOTTOM: {
			style.setVerticalAlignment(TextAlignmentType.BOTTOM);
			break;
		}
		case org.apache.poi.ss.usermodel.CellStyle.VERTICAL_TOP: {
			style.setVerticalAlignment(TextAlignmentType.TOP);
			break;
		}
		default: {
			style.setVerticalAlignment(TextAlignmentType.CENTER);
		}
		}

		// 边框类型
		BorderCollection borderCollection = new BorderCollection();
		style.setBorderCollection(borderCollection);
		// 上边框
		int borderType = poiStyle.getBorderTop();
		XSSFColor borderColor = poiStyle.getTopBorderXSSFColor();
		Border border = createBorder(borderType, borderColor);
		borderCollection.setByBorderType(BorderType.TOP_BORDER, border);
		// 下边框
		borderType = poiStyle.getBorderBottom();
		borderColor = poiStyle.getBottomBorderXSSFColor();
		border = createBorder(borderType, borderColor);
		borderCollection.setByBorderType(BorderType.BOTTOM_BORDER, border);
		// 左边框
		borderType = poiStyle.getBorderLeft();
		borderColor = poiStyle.getLeftBorderXSSFColor();
		border = createBorder(borderType, borderColor);
		borderCollection.setByBorderType(BorderType.LEFT_BORDER, border);
		// 右边框
		borderType = poiStyle.getBorderRight();
		borderColor = poiStyle.getRightBorderXSSFColor();
		border = createBorder(borderType, borderColor);
		borderCollection.setByBorderType(BorderType.RIGHT_BORDER, border);

		// 字体
		XSSFFont poiFont = poiStyle.getFont();
		style.setFont(readFont(poiFont));

		return style;
	}

	private Border createBorder(int borderType, XSSFColor borderColor) {
		Border border = new Border();
		if (borderColor != null) {
			border.setColor(readColor(borderColor));
		} else {
			border.setColor(Color.fromARGB(0, 0, 0));
		}

		switch (borderType) {
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_NONE: {
			border.setLineStyle(BorderLineType.NONE);
			break;
		}
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_THIN: {
			border.setLineStyle(BorderLineType.THIN);
			break;
		}
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_MEDIUM: {
			border.setLineStyle(BorderLineType.MEDIUM);
			break;
		}
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_DASHED: {
			border.setLineStyle(BorderLineType.DASHED);
			break;
		}
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_DOTTED: {
			border.setLineStyle(BorderLineType.DOTTED);
			break;
		}
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_THICK: {
			border.setLineStyle(BorderLineType.THICK);
			break;
		}
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_DOUBLE: {
			border.setLineStyle(BorderLineType.DOUBLE);
			break;
		}
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_HAIR: {
			border.setLineStyle(BorderLineType.HAIR);
			break;
		}
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_MEDIUM_DASHED: {
			border.setLineStyle(BorderLineType.MEDIUM_DASHED);
			break;
		}
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_DASH_DOT: {
			border.setLineStyle(BorderLineType.DASH_DOT);
			break;
		}
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_MEDIUM_DASH_DOT: {
			border.setLineStyle(BorderLineType.MEDIUM_DASH_DOT);
			break;
		}
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_DASH_DOT_DOT: {
			border.setLineStyle(BorderLineType.DASH_DOT_DOT);
			break;
		}
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_MEDIUM_DASH_DOT_DOT: {
			border.setLineStyle(BorderLineType.MEDIUM_DASH_DOT_DOT);
			break;
		}
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_SLANTED_DASH_DOT: {
			border.setLineStyle(BorderLineType.SLANTED_DASH_DOT);
			break;
		}
		default: {
			border.setLineStyle(BorderLineType.NONE);
		}
		}

		return border;
	}

	private Font readFont(XSSFFont poiFont) {
		Font font = new Font();
		XSSFColor color = poiFont.getXSSFColor();
		if (color == null) {
			font.setColor(Color.fromARGB(0, 0, 0));
		} else {
			font.setColor(readColor(color));
		}
		font.setName(poiFont.getFontName());
		font.setSize(poiFont.getFontHeightInPoints());
		font.setUnderline(poiFont.getUnderline() != org.apache.poi.ss.usermodel.Font.U_NONE);
		font.setItalic(poiFont.getItalic());
		font.setBold(poiFont.getBoldweight() == org.apache.poi.ss.usermodel.Font.BOLDWEIGHT_BOLD);
		return font;
	}

	private Color readColor(XSSFColor poiColor) {
		if (poiColor == null) {
			return null;
		}

		// 一定是argb
		byte[] rgb = poiColor.getARgb();
		if (rgb == null) {
			return Color.createDefaultColor();
		}
		return Color.fromARGB(rgb[0], rgb[1], rgb[2], rgb[3]);
	}

	public static final short EXCEL_COLUMN_WIDTH_FACTOR = 256;
	public static final int UNIT_OFFSET_LENGTH = 7;
	public static final int[] UNIT_OFFSET_MAP = new int[] { 0, 36, 73, 109,
			146, 182, 219 };

	/**
	 * pixel units to excel width units(units of 1/256th of a character width)
	 * 
	 * @param pxs
	 * @return
	 */
	public static short pixel2WidthUnits(int pxs) {
		short widthUnits = (short) (EXCEL_COLUMN_WIDTH_FACTOR * (pxs / UNIT_OFFSET_LENGTH));

		widthUnits += UNIT_OFFSET_MAP[(pxs % UNIT_OFFSET_LENGTH)];

		return widthUnits;
	}

	/**
	 * excel width units(units of 1/256th of a character width) to pixel units
	 * 
	 * @param widthUnits
	 * @return
	 */
	public static int widthUnits2Pixel(short widthUnits) {
		int pixels = (widthUnits / EXCEL_COLUMN_WIDTH_FACTOR)
				* UNIT_OFFSET_LENGTH;

		int offsetWidthUnits = widthUnits % EXCEL_COLUMN_WIDTH_FACTOR;
		pixels += Math.round((float) offsetWidthUnits
				/ ((float) EXCEL_COLUMN_WIDTH_FACTOR / UNIT_OFFSET_LENGTH));

		return pixels;
	}

}
