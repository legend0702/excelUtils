package cn.zhuhongqing.excel.cell;

public class Color {
    /**
     * 颜色的alpha值，此值控制了颜色的透明度
     */
    public int a;

    /**
     * 颜色的红分量值，Red
     */
    private int r = 0;
    /**
     * 颜色的绿分量值，Green
     */
    private int g = 0;
    /**
     * 颜色的蓝分量值，Blue
     */
    private int b = 0;

    public static Color createDefaultColor() {
        return Color.fromARGB(0, 0, 0);
    }

    public static Color fromARGB(int red, int green, int blue) {
        return new Color((int) 0xff, (int) red, (int) green, (int) blue);
    }

    public static Color fromARGB(int alpha, int red, int green, int blue) {
        return new Color(alpha, red, green, blue);
    }

    public Color(int a, int r, int g, int b) {
        this.a = a;
        this.b = b;
        this.r = r;
        this.g = g;
    }

    /**
     * 取得r的值
     * 
     * @return 返回r的值
     */
    public int getR() {
        return r;
    }

    /**
     * 取得g的值
     * 
     * @return 返回g的值
     */
    public int getG() {
        return g;
    }

    /**
     * 取得b的值
     * 
     * @return 返回b的值
     */
    public int getB() {
        return b;
    }

    public String getKey() {
        StringBuffer sb = new StringBuffer();
        sb.append('[');
        sb.append("r:");
        sb.append(r);
        sb.append(';');
        sb.append("g:");
        sb.append(g);
        sb.append(';');
        sb.append("b:");
        sb.append(b);
        sb.append(';');
        sb.append(']');
        return sb.toString();
    }
}
