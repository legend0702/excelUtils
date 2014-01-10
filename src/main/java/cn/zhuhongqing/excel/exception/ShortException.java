package cn.zhuhongqing.excel.exception;

public interface ShortException<E extends Exception, R extends RuntimeException> {

	public Class<E> getCatchExceptionType();

	public Class<R> getThrowExceptionType();

}
