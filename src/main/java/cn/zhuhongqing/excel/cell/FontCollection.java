package cn.zhuhongqing.excel.cell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

public class FontCollection {
	private Map<String, String> fontKey2IdMap = null;
	private List<Font> fonts = null;
	private Map<String, Font> fontMap = null;

	public FontCollection() {
		fontKey2IdMap = new HashMap<String, String>();
		fontMap = new HashMap<String, Font>();
		fonts = new ArrayList<Font>();
	}

	public Font addFont(Font font) {
		String fontId = font.getId();
		String fontKey = font.getKey();
		if (StringUtils.isEmpty(fontId)) {
			fontId = fontKey2IdMap.get(fontKey);
			if (fontId == null) {
				// 处理央视
				fontId = UUID.randomUUID().toString();
				fontKey2IdMap.put(fontKey, fontId);
				font.setId(fontId);
				fontMap.put(fontId, font);
				fonts.add(font);
			} else {
				font = getFont(fontId);
			}
		} else {
			if (containsStyle(fontId)) {
				font = getFont(fontId);
			} else {
				fontKey2IdMap.put(fontKey, fontId);
				fontMap.put(fontId, font);
				fonts.add(font);
			}
		}

		return font;
	}

	public Font getFont(String fontId) {
		return fontMap.get(fontId);
	}

	public boolean containsStyle(String fontId) {
		return fontMap.containsKey(fontId);
	}

	/**
	 * 取得styles的值
	 * 
	 * @return 返回styles的值
	 */
	public List<Font> getFonts() {
		return fonts;
	}

	public Font getFont(int index) {
		return fonts.get(index);
	}

	public int getFontCount() {
		return fonts.size();
	}
}
