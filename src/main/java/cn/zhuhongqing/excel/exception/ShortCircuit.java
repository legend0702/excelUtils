package cn.zhuhongqing.excel.exception;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import cn.zhuhongqing.excel.utils.GenericUtils;

/**
 * 异常短路机制
 * 
 * 捕捉Exception
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

public class ShortCircuit extends Exception {

	private static final long serialVersionUID = 1L;
	// 是否短路
	protected boolean shortCircuit = true;
	// 短路容量
	protected int shortSize = 0;
	// 短路key
	protected List<Object> shortKey = new ArrayList<Object>(0);
	// 存放异常
	protected List<Exception> throwableList = new ArrayList<Exception>(0);
	// 存放异常堆栈
	protected List<StackTraceElement> stackTraceList = new ArrayList<StackTraceElement>(
			0);

	public ShortCircuit() {
		super();
	}

	public ShortCircuit(String message) {
		super(message);
	}

	public ShortCircuit(Throwable throwable) {
		super(throwable);
	}

	public ShortCircuit(String message, Throwable throwable) {
		super(message, throwable);
	}

	public void addException(Exception exception) throws ShortCircuit {
		checkException(exception, null, null);
	}

	public void addException(String message) throws ShortCircuit {
		checkException(null, null, message);
	}

	public void addException(Object shortKey, Exception exception)
			throws ShortCircuit {
		checkException(exception, shortKey, null);
	}

	public void addException(Object shortKey, String message)
			throws ShortCircuit {
		checkException(null, shortKey, message);
	}

	public boolean hasException() {
		return (!getExceptionList().isEmpty());
	}

	public void tryThrowException() throws ShortCircuit {
		if (hasException()) {
			throw this;
		}
	}

	@Override
	public void printStackTrace() {
		printStackTrace(System.err);
	}

	@Override
	public void printStackTrace(PrintStream s) {
		Iterator<Exception> itr = getExceptionList().iterator();
		while (itr.hasNext()) {
			printEnclosedStackTrace(s, itr.next());
		}
	}

	private void printEnclosedStackTrace(PrintStream s, Exception exception) {
		StackTraceElement[] trace = exception.getStackTrace();
		int m = trace.length - 1, n = trace.length - 1;
		while (m >= 0 && n >= 0 && trace[m].equals(trace[n])) {
			m--;
			n--;
		}
		int framesInCommon = trace.length - 1 - m;

		s.println("Caused by: " + this);
		for (int i = 0; i <= m; i++)
			s.println("\tat " + trace[i]);
		if (framesInCommon != 0)
			s.println("\t... " + framesInCommon + " more");

	}

	public String getExceptionMessage() {
		StringBuffer stringBuffer = new StringBuffer();
		for (Exception t : getExceptionList()) {
			stringBuffer.append(t.getMessage());
			stringBuffer.append(StringUtils.CR);
			stringBuffer.append(StringUtils.LF);
		}
		return stringBuffer.toString();
	}

	protected void checkException(Exception exception, Object shortKey,
			String message) throws ShortCircuit {
		// 如果有异常 直接存入
		if (exception != null) {
			stackTraceList.addAll(Arrays.asList(exception.getStackTrace()));
			this.throwableList.add(exception);
			// 如果只有消息 则创建自身,再存入
		} else if (message != null) {
			ShortCircuit shortCircuit = createShortCircuit(null, message);
			stackTraceList.addAll(Arrays.asList(shortCircuit.getStackTrace()));
			this.throwableList.add(shortCircuit);
		}

		// 短路直接抛异常
		if (isShortCircuit()) {
			tryThrowException();
		}
		// 不短路的情况
		// 如果容量是空且没有短路key 则直接返回 什么也不做
		if (isEmptyShortSize() && isEmptyShortKey()) {
			return;
		}

		// 如果有短路key 且匹配到了 直接抛出异常
		if (keyContains(shortKey) && (!isEmptyShortKey())) {
			tryThrowException();
		}

		// 如果容量满了直接抛出
		if (getShortSize() == getExceptionList().size()) {
			tryThrowException();
		}

	}

	protected ShortCircuit createShortCircuit(Exception exception,
			String message) {

		Map<Class<?>, Object> constructorTypeAndParam = new HashMap<Class<?>, Object>();

		constructorTypeAndParam.put(String.class, message);

		constructorTypeAndParam.put(Throwable.class, exception);

		try {
			return GenericUtils.autoCreateObject(this.getClass(),
					constructorTypeAndParam);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public boolean keyContains(Object shortKey) {
		return this.shortKey.contains(shortKey);
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

	public void addShortKey(Object shortKey) {
		this.shortKey.add(shortKey);
	}

	public List<Exception> getExceptionList() {
		return throwableList;
	}

	public void setExceptionList(List<Exception> throwableList) {
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
