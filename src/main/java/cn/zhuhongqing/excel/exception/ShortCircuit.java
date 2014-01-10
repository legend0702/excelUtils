package cn.zhuhongqing.excel.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import cn.zhuhongqing.excel.utils.GenericUtils;

/**
 * 异常短路机制
 * 
 * 捕捉Throwable
 * 
 * 以运行时异常的形式刨出ShortCircuit
 * 
 * 默认短路
 * 
 * 无短路key
 * 
 * 短路容量为0
 * 
 */

public class ShortCircuit extends RuntimeException {

	private static final long serialVersionUID = 1L;
	// 是否短路
	private boolean shortCircuit = true;
	// 短路容量
	private int shortSize = 0;
	// 短路key
	private List<String> shortKey = new ArrayList<String>();
	// 存放异常
	private List<Throwable> throwableList = new ArrayList<Throwable>();

	public ShortCircuit() {
		super();
	}

	public ShortCircuit(String message, Throwable cause) {
		super(message, cause);
	}

	public ShortCircuit(String message) {
		super(message);
	}

	public ShortCircuit(Throwable cause) {
		super(cause);
	}

	public void addThrowable(String key, Throwable exception) {
		checkException(exception, key, null);
	}

	public void addThrowable(String key, String message) {
		checkException(null, key, message);
	}

	public void addThrowable(Throwable exception) {
		checkException(exception, null, null);
	}

	public void addThrowable(String message) {
		checkException(null, null, message);
	}

	public boolean hasException() {
		return (!getThrowableList().isEmpty());
	}

	public void tryThrowException() {
		if (hasException()) {
			throw createRuntimeException(null, getExceptionMessage());
		}
	}

	public String getExceptionMessage() {
		StringBuffer stringBuffer = new StringBuffer();
		for (Throwable t : getThrowableList()) {
			stringBuffer.append(t.getMessage());
			stringBuffer.append(StringUtils.CR);
			stringBuffer.append(StringUtils.LF);
		}
		return stringBuffer.toString();
	}

	private void checkException(Throwable throwable, String key, String message) {
		if (isShortCircuit()) {
			throwRuntimeException(throwable, message);
		} else {
			if (isEmptyShortSize() || isEmptyShortKey()) {
				throwRuntimeException(throwable, message);
			} else {
				if (getShortSize() == getThrowableList().size()) {
					throwRuntimeException(throwable, message);
				}
				if (keyContains(key)) {
					throwRuntimeException(throwable, message);
				}
			}
		}

		if (throwable != null) {
			this.throwableList.add(throwable);
			return;
		}

		if (message != null) {
			this.throwableList.add(createRuntimeException(null, message));
			return;
		}
	}

	private void throwRuntimeException(Throwable exception, String message) {
		throw createRuntimeException(exception, message);
	}

	private RuntimeException createRuntimeException(Throwable exception,
			String message) {

		try {

			Map<Class<?>, Object> constructorTypeAndParam = new HashMap<Class<?>, Object>();

			constructorTypeAndParam.put(String.class, message);

			constructorTypeAndParam.put(Throwable.class, exception);

			return GenericUtils.autoCreateObject(this.getClass(),
					constructorTypeAndParam);

		} catch (Exception e) {
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
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

	public boolean isEmptyShortKey() {
		return this.shortKey.isEmpty();
	}

	public void addShortKey(String key) {
		this.shortKey.add(key);
	}

	public List<Throwable> getThrowableList() {
		return throwableList;
	}

	public void setThrowableList(List<Throwable> throwableList) {
		this.throwableList = throwableList;
	}

	public boolean isEmptyShortSize() {
		return getShortSize() == 0;
	}

	public int getShortSize() {
		return shortSize;
	}

	public void setShortSize(int shortSize) {
		this.shortSize = shortSize;
	}

}
