package cn.zhuhongqing.excel.cell;

public class Border {
    private Color color = null;
    private int lineStyle = 0;

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
     * 取得style的值
     * 
     * @return 返回style的值
     */
    public int getLineStyle() {
        return lineStyle;
    }

    /**
     * 设置style的值
     * 
     * @param style
     *            style的新值
     */
    public void setLineStyle(int style) {
        this.lineStyle = style;
    }

    public Object getKey() {
        StringBuffer sb = new StringBuffer();
        sb.append("Border[");
        if (color != null) {
            sb.append("color:");
            sb.append(color.getKey());
            sb.append(';');
        }

        sb.append("style : ");
        sb.append(lineStyle);
        sb.append(';');
        sb.append(']');
        return sb.toString();
    }
}
