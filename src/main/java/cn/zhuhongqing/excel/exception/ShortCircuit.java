package cn.zhuhongqing.excel.exception;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.zhuhongqing.excel.utils.GenericUtils;

/**
 * 异常短路机制
 * 
 * 捕捉T类型的exception
 * 
 * 抛出k类型的运行时异常
 * 
 * 默认短路
 * 
 * 无短路key
 * 
 * @param <K>
 * 
 * @param <K>
 */

public abstract class ShortCircuit<E extends Exception, R extends RuntimeException>
		implements ShortException<E, R> {

	private Class<E> catchException = null;
	private Class<R> throwException = null;
	private boolean shortCircuit = true;
	private List<String> shortKey = new ArrayList<String>();
	private List<E> exceptionList = new ArrayList<E>();

	public void addException(String key, E exception) {
		checkException(exception, key, null);
	}

	public void addException(String key, String message) {

	}

	public void addException(E exception) {
		checkException(exception, null, null);
	}

	public boolean hasException() {
		return (!getExceptionList().isEmpty());
	}

	public void tryThrowException() {
		if (hasException()) {
			throw createRuntimeException(null, getExceptionMessage());
		}
	}

	public String getExceptionMessage() {
		StringBuffer stringBuffer = new StringBuffer();
		for (E e : getExceptionList()) {
			stringBuffer.append(e.getMessage());
			stringBuffer.append("\r\n");
		}
		return stringBuffer.toString();
	}

	private void throwRuntimeException(E exception, String message) {
		throw createRuntimeException(exception, null);
	}

	private void checkException(E exception, String key, String message) {
		if (isShortCircuit()) {
			throwRuntimeException(exception, message);
		} else {
			if (isEmptyShortKey()) {
				throwRuntimeException(exception, message);
			}
			if (keyContains(key)) {
				throwRuntimeException(exception, message);
			}
		}

		if (exception != null) {
			this.exceptionList.add(exception);
			return;
		}
		// if (message != null) {
		// this.exceptionList.add(createException(message));
		// return;
		// }
	}

	private R createRuntimeException(E exception, String message) {

		try {

			Map<Class<?>, Object> constructorTypeAndParam = new HashMap<Class<?>, Object>();

			constructorTypeAndParam.put(String.class, message);

			constructorTypeAndParam.put(Throwable.class, exception);

			return GenericUtils.autoCreateObject(getThrowExceptionType(),
					constructorTypeAndParam);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return getExceptionMessage();
	}

	public boolean keyContains(String key) {
		return shortKey.contains(key);
	}

	public boolean isShortCircuit() {
		return shortCircuit;
	}

	public void setShortCircuit(boolean shortCircuit) {
		this.shortCircuit = shortCircuit;
	}

	public List<E> getExceptionList() {
		return exceptionList;
	}

	public void setExceptionList(List<E> exceptionList) {
		this.exceptionList = exceptionList;
	}

	public boolean isEmptyShortKey() {
		return this.shortKey.isEmpty();
	}

	public void addShortKey(String key) {
		this.shortKey.add(key);
	}

	@SuppressWarnings("unchecked")
	public Class<E> getCatchExceptionType() {
		if (catchException == null) {
			ParameterizedType superClassParameterType = (ParameterizedType) this
					.getClass().getGenericSuperclass();
			Class<E> eClass = (Class<E>) superClassParameterType
					.getActualTypeArguments()[0];
			catchException = eClass;
		}
		return catchException;
	}

	@SuppressWarnings("unchecked")
	public Class<R> getThrowExceptionType() {
		if (throwException == null) {
			ParameterizedType superClassParameterType = (ParameterizedType) this
					.getClass().getGenericSuperclass();
			Class<R> rClass = (Class<R>) superClassParameterType
					.getActualTypeArguments()[1];
			throwException = rClass;
		}
		return throwException;
	}

}
