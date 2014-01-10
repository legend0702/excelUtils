package cn.zhuhongqing.excel.cell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

public class StyleCollection {
	private Map<String, String> styleKey2IdMap = null;
	private List<Style> styles = null;
	private Map<String, Style> styleMap = null;
	private Style defaultStyle = null;

	private FontCollection fontCollection = null;

	public StyleCollection() {
		styleKey2IdMap = new HashMap<String, String>();
		styleMap = new HashMap<String, Style>();
		styles = new ArrayList<Style>();

		fontCollection = new FontCollection();
	}

	public Style addStyle(Style style) {
		String styleId = style.getId();
		String styleKey = style.getKey();
		if (StringUtils.isEmpty(styleId)) {
			styleId = styleKey2IdMap.get(styleKey);
			if (styleId == null) {
				// 处理央视
				styleId = UUID.randomUUID().toString();
				styleKey2IdMap.put(styleKey, styleId);
				style.setId(styleId);
				styleMap.put(styleId, style);
				styles.add(style);
			} else {
				style = getStyle(styleId);
			}
		} else {
			if (containsStyle(styleId)) {
				style = getStyle(styleId);
			} else {
				styleKey2IdMap.put(styleKey, styleId);
				styleMap.put(styleId, style);
				styles.add(style);
			}
		}

		// 处理字体
		Font font = style.getFont();
		font = fontCollection.addFont(font);
		style.setFont(font);

		return style;
	}

	public Style getStyle(String styleId) {
		return styleMap.get(styleId);
	}

	public boolean containsStyle(String styleId) {
		return styleMap.containsKey(styleId);
	}

	/**
	 * 取得styles的值
	 * 
	 * @return 返回styles的值
	 */
	public List<Style> getStyles() {
		return styles;
	}

	public int getStyleCount() {
		return styles.size();
	}

	public Style getStyle(int index) {
		return styles.get(index);
	}

	/**
	 * 取得defaultStyle的值
	 * 
	 * @return 返回defaultStyle的值
	 */
	public Style getDefaultStyle() {
		return defaultStyle;
	}

	/**
	 * 设置defaultStyle的值
	 * 
	 * @param defaultStyle
	 *            defaultStyle的新值
	 */
	public void setDefaultStyle(Style defaultStyle) {
		this.defaultStyle = defaultStyle;
		addStyle(defaultStyle);
	}

	/**
	 * 取得fonts的值
	 * 
	 * @return 返回fonts的值
	 */
	public List<Font> getFonts() {
		return fontCollection.getFonts();
	}

	public Font addFont(Font font) {
		return fontCollection.addFont(font);
	}

	public Font getFont(String fontId) {
		return fontCollection.getFont(fontId);
	}

	public int getFontCount() {
		return fontCollection.getFontCount();
	}

	public Font getFont(int index) {
		return fontCollection.getFont(index);
	}
}
