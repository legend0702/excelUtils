package cn.zhuhongqing.excel.cell;

public class Font {
    private String id = null;
    // private int capsType = -1;
    private Color color = null;
    private String name = null;
    // private double doubleSize = 0;
    // private int strikeType = -1;
    private int size = 0;
    private boolean italic = false;
    private boolean bold = false;
    private boolean underline = false;

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

    // /**
    // * 取得capsType的值
    // *
    // * @return 返回capsType的值
    // */
    // public int getCapsType() {
    // return capsType;
    // }
    //
    // /**
    // * 设置capsType的值
    // *
    // * @param capsType capsType的新值
    // */
    // public void setCapsType(int capsType) {
    // this.capsType = capsType;
    // }

    /**
     * 取得color的值
     * 
     * @return 返回color的值
     */
    public Color getColor() {
        return color;
    }

    /**
     * 设置color的值
     * 
     * @param color
     *            color的新值
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * 取得name的值
     * 
     * @return 返回name的值
     */
    public String getName() {
        return name;
    }

    /**
     * 设置name的值
     * 
     * @param name
     *            name的新值
     */
    public void setName(String name) {
        this.name = name;
    }

    // /**
    // * 取得doubleSize的值
    // *
    // * @return 返回doubleSize的值
    // */
    // public double getDoubleSize() {
    // return doubleSize;
    // }
    //
    // /**
    // * 设置doubleSize的值
    // *
    // * @param doubleSize doubleSize的新值
    // */
    // public void setDoubleSize(double doubleSize) {
    // this.doubleSize = doubleSize;
    // }
    //
    // /**
    // * 取得strikeType的值
    // *
    // * @return 返回strikeType的值
    // */
    // public int getStrikeType() {
    // return strikeType;
    // }
    //
    // /**
    // * 设置strikeType的值
    // *
    // * @param strikeType strikeType的新值
    // */
    // public void setStrikeType(int strikeType) {
    // this.strikeType = strikeType;
    // }

    /**
     * 取得size的值
     * 
     * @return 返回size的值
     */
    public int getSize() {
        return size;
    }

    /**
     * 设置size的值
     * 
     * @param size
     *            size的新值
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * 取得underline的值
     * 
     * @return 返回underline的值
     */
    public boolean isUnderline() {
        return underline;
    }

    /**
     * 设置underline的值
     * 
     * @param underline
     *            underline的新值
     */
    public void setUnderline(boolean underline) {
        this.underline = underline;
    }

    /**
     * 取得italic的值
     * 
     * @return 返回italic的值
     */
    public boolean isItalic() {
        return italic;
    }

    /**
     * 设置italic的值
     * 
     * @param italic
     *            italic的新值
     */
    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    /**
     * 取得bold的值
     * 
     * @return 返回bold的值
     */
    public boolean isBold() {
        return bold;
    }

    /**
     * 设置bold的值
     * 
     * @param bold
     *            bold的新值
     */
    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public String getKey() {
        StringBuffer sb = new StringBuffer();
        sb.append("Font[");
        // sb.append("capsType:");
        // sb.append(capsType);
        // sb.append(';');
        if (color != null) {
            sb.append("color:");
            sb.append(color.getKey());
            sb.append(';');
        }

        sb.append("name:");
        sb.append(name);
        sb.append(';');

        // sb.append("doubleSize:");
        // sb.append(doubleSize);
        // sb.append(';');
        //
        // sb.append("strikeType:");
        // sb.append(strikeType);
        // sb.append(';');

        sb.append("size:");
        sb.append(size);
        sb.append(';');

        sb.append("italic:");
        sb.append(italic);
        sb.append(';');

        sb.append("bold:");
        sb.append(bold);
        sb.append(';');

        sb.append("underline:");
        sb.append(underline);
        sb.append(';');
        sb.append(']');
        return sb.toString();
    }

    public String toString() {
        return getKey();
    }

    public static Font createDefaultFont() {
        Font font = new Font();
        font.setColor(Color.createDefaultColor());
        font.setName("宋体");
        font.setSize(11);
        font.setItalic(false);
        font.setBold(false);
        font.setUnderline(false);

        return font;
    }
}
