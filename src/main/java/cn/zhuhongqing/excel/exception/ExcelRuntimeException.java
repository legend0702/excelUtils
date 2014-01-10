package cn.zhuhongqing.excel.exception;

public class ExcelRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExcelRuntimeException() {
		super();
	}

	public ExcelRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExcelRuntimeException(String message) {
		super(message);
	}

	public ExcelRuntimeException(Throwable cause) {
		super(cause);
	}

}
