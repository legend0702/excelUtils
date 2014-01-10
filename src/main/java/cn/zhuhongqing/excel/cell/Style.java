package cn.zhuhongqing.excel.cell;

public class Style {
    private static Style style = null;
    private String id = null;
    private int horizontalAlignment = 0;
    private int verticalAlignment = 0;
    // private Color foregroundColor = null;
    private Color backgroundColor = null;
    private BorderCollection borderCollection = null;
    private Font font = null;

    public static Style createDefaultStyle() {
        if (style == null) {
            style = new Style();
            style.setHorizontalAlignment(TextAlignmentType.GENERAL);
            style.setVerticalAlignment(TextAlignmentType.CENTER);
            style.setFont(Font.createDefaultFont());
            style.setBorderCollection(BorderCollection.createDefaultborders());
        }
        return style;
    }

    /**
     * 取得id的值
     * 
     * @return 返回id的值
     */
    public String getId() {
        return id;
    }

    /**
     * 设置id的值
     * 
     * @param id
     *            id的新值
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 取得horizontalAlignment的值
     * 
     * @return 返回horizontalAlignment的值
     */
    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }

    /**
     * 设置horizontalAlignment的值
     * 
     * @param horizontalAlignment
     *            horizontalAlignment的新值
     */
    public void setHorizontalAlignment(int horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    /**
     * 取得verticalAlignment的值
     * 
     * @return 返回verticalAlignment的值
     */
    public int getVerticalAlignment() {
        return verticalAlignment;
    }

    /**
     * 设置verticalAlignment的值
     * 
     * @param verticalAlignment
     *            verticalAlignment的新值
     */
    public void setVerticalAlignment(int verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    // /**
    // * 取得foregroundColor的值
    // *
    // * @return 返回foregroundColor的值
    // */
    // public Color getForegroundColor() {
    // return foregroundColor;
    // }
    //
    // /**
    // * 设置foregroundColor的值
    // *
    // * @param foregroundColor foregroundColor的新值
    // */
    // public void setForegroundColor(Color foregroundColor) {
    // this.foregroundColor = foregroundColor;
    // }

    /**
     * 取得backgroundColor的值
     * 
     * @return 返回backgroundColor的值
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * 设置backgroundColor的值
     * 
     * @param backgroundColor
     *            backgroundColor的新值
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * 取得borderCollection的值
     * 
     * @return 返回borderCollection的值
     */
    public BorderCollection getBorderCollection() {
        return borderCollection;
    }

    /**
     * 设置borderCollection的值
     * 
     * @param borderCollection
     *            borderCollection的新值
     */
    public void setBorderCollection(BorderCollection borderCollection) {
        this.borderCollection = borderCollection;
    }

    /**
     * 取得font的值
     * 
     * @return 返回font的值
     */
    public Font getFont() {
        return font;
    }

    /**
     * 设置font的值
     * 
     * @param font
     *            font的新值
     */
    public void setFont(Font font) {
        this.font = font;
    }

    public String getKey() {
        StringBuffer sb = new StringBuffer();
        sb.append("Style[");
        sb.append("horizontalAlignment:");
        sb.append(horizontalAlignment);
        sb.append(';');

        sb.append("verticalAlignment:");
        sb.append(verticalAlignment);
        sb.append(';');

        // sb.append("foregroundColor:");
        // sb.append(foregroundColor.getKey());
        // sb.append(';');

        if (backgroundColor != null) {
            sb.append("backgroundColor:");
            sb.append(backgroundColor.getKey());
            sb.append(';');
        }

        sb.append(font.getKey());
        sb.append(borderCollection.getKey());

        sb.append(']');

        return sb.toString();
    }

    public String toString() {
        return getKey();
    }
}
