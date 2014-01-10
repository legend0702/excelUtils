package cn.zhuhongqing.excel.exception;

import java.io.PrintStream;
import java.util.ArrayList;
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
 * 默认短路(立刻抛出异常)
 * 
 * 无短路key(则不会因为短路key抛出异常)
 * 
 * 短路容量为0(0代表无限)
 * 
 * 如果出现异常 则将自己抛出
 * 
 * @author zhq mail:qwepoidjdj(a)hotmail.com
 * @since 1.6
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

	// 提供构造方法
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

	/**
	 * 加入异常队列
	 * 
	 * 默认以该异常为短路key(shortKey)以测试短路
	 * 
	 * 重载{@link ShortCircuit#checkException(Exception, Object, String)}
	 * 
	 * @param exception
	 *            被包装的异常
	 * @throws ShortCircuit
	 */
	public void addException(Exception exception) throws ShortCircuit {
		checkException(exception, exception, null);
	}

	/**
	 * 加入异常队列
	 * 
	 * 默认以该message为短路key(shortKey)以测试短路
	 * 
	 * 重载{@link ShortCircuit#checkException(Exception, Object, String)}
	 * 
	 * @param message
	 *            异常消息
	 * @throws ShortCircuit
	 */
	public void addException(String message) throws ShortCircuit {
		checkException(null, message, message);
	}

	/**
	 * 加入异常队列
	 * 
	 * 以shortKey为短路key测试短路
	 * 
	 * 重载{@link ShortCircuit#checkException(Exception, Object, String)}
	 * 
	 * @param shortKey
	 *            短路key
	 * @param exception
	 *            被包装的异常
	 * @throws ShortCircuit
	 */
	public void addException(Object shortKey, Exception exception)
			throws ShortCircuit {
		checkException(exception, shortKey, null);
	}

	/**
	 * 加入异常队列
	 * 
	 * 以shortKey为短路key测试短路
	 * 
	 * 重载{@link ShortCircuit#checkException(Exception, Object, String)}
	 * 
	 * @param shortKey
	 *            短路key
	 * @param message
	 *            异常消息
	 * @throws ShortCircuit
	 */
	public void addException(Object shortKey, String message)
			throws ShortCircuit {
		checkException(null, shortKey, message);
	}

	/**
	 * 异常队列里是否存在异常
	 * 
	 * @return
	 */
	public boolean hasException() {
		return (!getExceptionList().isEmpty());
	}

	/**
	 * 尝试抛处异常(自身)
	 * 
	 * 如果没有 则什么反映也没有
	 * 
	 * @throws ShortCircuit
	 */
	public void tryThrowException() throws ShortCircuit {
		if (hasException()) {
			throw this;
		}
	}

	// 重写父类异常打印机制
	@Override
	public void printStackTrace() {
		printStackTrace(System.err);
	}

	// 重写父类异常打印机制
	// 打印异常堆摘中的异常
	// 最先加入的异常在最下面(类似堆)
	@Override
	public void printStackTrace(PrintStream s) {
		// 如果异常堆栈没有异常 则打印自己
		if (getExceptionList().isEmpty()) {
			printEnclosedStackTrace(s, this);
			return;
		}
		// 循环打印异常堆栈
		Iterator<Exception> itr = getExceptionList().iterator();
		while (itr.hasNext()) {
			printEnclosedStackTrace(s, itr.next());
		}
	}

	/**
	 * 检测异常
	 * 
	 * @param exception
	 *            加入队列的异常
	 * @param shortKey
	 *            匹配短路key
	 * @param message
	 *            异常信息
	 * @throws ShortCircuit
	 */

	protected void checkException(Exception exception, Object shortKey,
			String message) throws ShortCircuit {
		// 如果有异常 包装成自身 再存入
		if (exception != null) {
			ShortCircuit shortCircuit = createShortCircuit(exception,
					exception.getMessage());
			shortCircuit.setStackTrace(exception.getStackTrace());
			getExceptionList().add(shortCircuit);
			// 如果只有消息 则创建自身,再存入
		} else if (message != null) {
			getExceptionList().add(createShortCircuit(null, message));
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

	/**
	 * 把exception包装后 创建自身
	 * 
	 * 如果exception没有 则以message创建自身
	 * 
	 * 如果什么都没有 则以RuntimeException包装系统异常抛出
	 * 
	 * @param exception
	 *            被包装的异常
	 * @param message
	 *            被采用的异常信息
	 * @return
	 */
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

	// 打印异常
	private void printEnclosedStackTrace(PrintStream s, Exception exception) {
		StackTraceElement[] trace = exception.getStackTrace();
		// 如果有包装异常 则打印包装异常
		// 没有则打印自身
		Throwable causeException = exception.getCause();
		s.println("Caused by: "
				+ (causeException == null ? exception.getClass()
						: causeException.getClass())
				+ ":"
				+ (causeException == null ? exception.getMessage()
						: causeException.getMessage()));
		for (int i = 0; i < trace.length; i++)
			s.println("\tat " + trace[i]);
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

	public String getExceptionMessage() {
		StringBuffer stringBuffer = new StringBuffer();
		for (Exception t : getExceptionList()) {
			stringBuffer.append(t.getMessage());
			stringBuffer.append(StringUtils.CR);
			stringBuffer.append(StringUtils.LF);
		}
		return stringBuffer.toString();
	}

	@Override
	public String toString() {
		printStackTrace();
		return "";
	}

}
