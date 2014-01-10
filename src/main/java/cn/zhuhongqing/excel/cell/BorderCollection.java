package cn.zhuhongqing.excel.cell;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BorderCollection {
    private Map<Integer, Border> borderMap = null;

    public BorderCollection() {
        borderMap = new HashMap<Integer, Border>();
    }

    public Border getByBorderType(int borderType) {
        return borderMap.get(borderType);
    }

    public void setByBorderType(int borderType, Border border) {
        borderMap.put(borderType, border);
    }

    public void setColor(Color color) {
        Collection<Border> borders = borderMap.values();
        if (borders != null) {
            for (Border border : borders) {
                border.setColor(color);
            }
        }
    }

    public void setStyle(int style) {
        Collection<Border> borders = borderMap.values();
        if (borders != null) {
            for (Border border : borders) {
                border.setLineStyle(style);
            }
        }
    }

    public String getKey() {
        StringBuffer sb = new StringBuffer();
        sb.append("Borders[");
        for (int type : BorderType.ALL_TYPES) {
            Border border = borderMap.get(type);
            sb.append(type);
            sb.append(':');
            sb.append(border.getKey());
            sb.append(';');
        }
        sb.append("];");
        return sb.toString();
    }

    public String toString() {
        return getKey();
    }

    public static BorderCollection createDefaultborders() {
        BorderCollection borderCollection = new BorderCollection();
        Border topBorder = new Border();
        topBorder.setColor(Color.fromARGB(0, 0, 0));
        topBorder.setLineStyle(BorderLineType.NONE);
        borderCollection.setByBorderType(BorderType.TOP_BORDER, topBorder);

        Border bottomBorder = new Border();
        bottomBorder.setColor(Color.fromARGB(0, 0, 0));
        bottomBorder.setLineStyle(BorderLineType.NONE);
        borderCollection
                .setByBorderType(BorderType.BOTTOM_BORDER, bottomBorder);

        Border leftBorder = new Border();
        leftBorder.setColor(Color.fromARGB(0, 0, 0));
        leftBorder.setLineStyle(BorderLineType.NONE);
        borderCollection.setByBorderType(BorderType.LEFT_BORDER, leftBorder);

        Border rightBorder = new Border();
        rightBorder.setColor(Color.fromARGB(0, 0, 0));
        rightBorder.setLineStyle(BorderLineType.NONE);
        borderCollection.setByBorderType(BorderType.RIGHT_BORDER, rightBorder);

        return borderCollection;
    }
}
