package cn.zhuhongqing.excel.utils.bean;

public class DateReg {

	private String pattern;

	private String dateReg;

	public DateReg() {

	}

	public DateReg(String pattern, String dateReg) {
		setPattern(pattern);
		setDateReg(dateReg);
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getDateReg() {
		return dateReg;
	}

	public void setDateReg(String dateReg) {
		this.dateReg = dateReg;
	}

}
