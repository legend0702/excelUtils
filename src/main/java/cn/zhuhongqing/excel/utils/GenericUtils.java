package cn.zhuhongqing.excel.utils;

import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.apache.commons.lang3.math.NumberUtils;

import cn.zhuhongqing.excel.utils.bean.DateReg;
import cn.zhuhongqing.excel.utils.bean.OrderedProperties;

public class GenericUtils {

	public static final String APPLICATION_ENCODE = "UTF-8";

	public static DateReg[] EXCEL_DATE_FORMATTER = null;

	static {
		// Excel日期解析
		if (EXCEL_DATE_FORMATTER == null) {
			EXCEL_DATE_FORMATTER = new DateReg[] {
					new DateReg(
							"yyyyMMdd",
							"(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))0229)"),
					new DateReg(
							"yyyy-MM-dd",
							"(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)"),
					new DateReg(
							"yyyy/MM/dd",
							"(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})/(((0[13578]|1[02])/(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)/(0[1-9]|[12][0-9]|30))|(02/(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))/02/29)"),
					new DateReg(
							"yyyy-M-dd",
							"(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-((([1-9])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)") };
		}

	}

	public static boolean isJson(String json) {
		return (isMapJson(json) || isArrayJson(json));
	}

	public static boolean isMapJson(String json) {
		if (json != null && json.startsWith("{") && json.endsWith("}")) {
			return true;
		}
		return false;
	}

	public static boolean isArrayJson(String json) {
		if (json != null && json.startsWith("[") && json.endsWith("]")) {
			return true;
		}
		return false;
	}

	public static boolean isNull(Object... objs) {
		for (Object obj : objs) {
			if (obj == null) {
				return true;
			}
		}
		return false;
	}

	public static boolean isNotNull(Object... objs) {
		return (!isNull(objs));
	}

	public static Object convertDate(String dateStr, DateReg[] dateRegs) {

		Date returnDate = null;

		for (DateReg dateReg : dateRegs) {

			if (dateStr.length() == dateReg.getPattern().length()) {

				if (dateStr.matches(dateReg.getDateReg())) {

					if (returnDate == null) {
						synchronized (Thread.currentThread()) {
							DateFormat dateFormat = new SimpleDateFormat(
									dateReg.getPattern());
							try {
								returnDate = dateFormat.parse(dateStr);
							} catch (ParseException e) {
								continue;
							}
						}
					} else {
						break;
					}
				}
			}
		}

		if (returnDate == null) {
			return dateStr;
		}

		return returnDate;
	}

	/**
	 * 获得所有泛型
	 * 
	 * @param originClass
	 *            目标类
	 * 
	 */

	public static List<Class<?>> getSuperGenericClasses(Class<?> originClass) {

		List<Class<?>> genericClassList = new ArrayList<Class<?>>();

		ParameterizedType superClassParameterType = (ParameterizedType) originClass
				.getGenericSuperclass();

		ParameterizedType[] interfaceClassParameterTypes = (ParameterizedType[]) originClass
				.getGenericInterfaces();

		if (superClassParameterType != null) {
			for (Class<?> genericClass : (Class<?>[]) superClassParameterType
					.getActualTypeArguments()) {
				genericClassList.add(genericClass);
			}
		}

		if (interfaceClassParameterTypes != null
				&& interfaceClassParameterTypes.length > 0) {
			for (ParameterizedType interfaceClassParameterType : interfaceClassParameterTypes) {
				for (Class<?> genericClass : (Class<?>[]) interfaceClassParameterType
						.getActualTypeArguments()) {
					genericClassList.add(genericClass);
				}
			}
		}
		return genericClassList;
	}

	/**
	 * 根据传入的 Map<Class<?>,Object> constructorTypeAndParam
	 * 
	 * key为构造函数的参数类型,value为构造函数的参数
	 * 
	 * 自动匹配构造函数创建对象并返回
	 * 
	 * 由于以类型为key,所以不支持多个参数类型一样的 减少复杂度
	 * 
	 * 如果有构造参数类型一样 位置不一样的 返回第一个匹配到的
	 * 
	 * 不支持无参构造
	 * 
	 * @param tClass
	 *            创建的对象类型
	 * @param constructorTypeAndParam
	 *            构造器类型
	 * @throws Exception
	 *             非法参数,没有找到适合的构造方法,创建对象失败等都会抛出异常
	 */

	@SuppressWarnings("unchecked")
	public static <T> T autoCreateObject(Class<T> tClass,
			Map<Class<?>, Object> constructorTypeAndParam) throws Exception {

		constructorTypeAndParam = filterMap(constructorTypeAndParam);

		if (constructorTypeAndParam.isEmpty()) {
			throw new IllegalArgumentException(
					"constructorTypeAndParam is empty");
		}

		Constructor<T>[] constructors = (Constructor<T>[]) tClass
				.getConstructors();
		List<Object> constructorParams = new ArrayList<Object>();
		for (Constructor<T> constructor : constructors) {
			Class<?>[] paramTypes = constructor.getParameterTypes();
			if (paramTypes.length == constructorTypeAndParam.size()) {
				for (Class<?> paramType : paramTypes) {
					if (constructorTypeAndParam.containsKey(paramType)) {
						constructorParams.add(constructorTypeAndParam
								.get(paramType));
					}
				}
				if (constructorParams.size() == constructorTypeAndParam.size()) {
					if (!constructor.isAccessible()) {
						constructor.setAccessible(true);
					}
					try {
						return constructor.newInstance(constructorParams
								.toArray());
					} finally {
						constructor.setAccessible(false);
						constructorParams.clear();
						constructorParams = null;
					}
				}
				constructorParams.clear();
			}
		}
		constructorParams.clear();
		constructorParams = null;
		throw new NoSuchMethodException("con not find a Constructor");
	}

	/**
	 * 过滤空map
	 * 
	 * key为null 以及value为null的
	 * 
	 * @param targetMap
	 *            目标map
	 * @return
	 */

	public static <K, V> Map<K, V> filterMap(Map<K, V> targetMap) {
		Map<K, V> filerMap = new HashMap<K, V>();
		Iterator<Entry<K, V>> mapItr = targetMap.entrySet().iterator();
		while (mapItr.hasNext()) {
			Entry<K, V> mapEntry = mapItr.next();
			if (mapEntry.getKey() != null && mapEntry.getValue() != null) {
				filerMap.put(mapEntry.getKey(), mapEntry.getValue());
			}
		}
		return filerMap;
	}

	/**
	 * 数据库字段于bean字段中的对应关系: 数据库字典变小写 然后下划线后面第一个大写
	 * 
	 * 例:DB_KEY ==> dbKey
	 * 
	 * @param str
	 * @param split
	 * @return str
	 */

	public static String splitFirstUpSubLine(String str, String split) {

		String strTemp = "";

		StringTokenizer stringTokenizer = new StringTokenizer(str, split);

		while (stringTokenizer.hasMoreElements()) {
			if (strTemp.length() < 1) {
				strTemp = stringTokenizer.nextToken();
			} else {
				strTemp += StringUtils
						.firstToUpper(stringTokenizer.nextToken());
			}
		}
		if (StringUtils.isNull(strTemp)) {
			return str;
		}
		return strTemp;
	}

	/**
	 * 主要处理key名字
	 * 
	 * 把不符合项目的名字转换成项目中需要的名字
	 * 
	 * @param propName
	 * @return
	 */

	public static String createPropName(String propName) {
		return propName;
	}

	/**
	 * 正则匹配数字
	 * 
	 * 整数以及99个小数..
	 * 
	 * @param strNumber
	 * @return
	 */

	public static boolean isNumber(String strNumber) {
		return NumberUtils.isNumber(strNumber);
	}

	public static Number createNumber(String str) {
		return NumberUtils.createNumber(str);
	}

	public static Object mapValueGetKey(Map<?, ?> map, Object value) {
		if (!map.containsValue(value)) {
			return null;
		}
		Iterator<?> itr = map.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) itr.next();
			if (entry.getValue().equals(value)) {
				return entry.getKey();
			}
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, String> loadProp(String propName) {

		OrderedProperties prop = new OrderedProperties();

		try {
			prop.load(new InputStreamReader(GenericUtils.class.getClassLoader()
					.getResourceAsStream(propName), APPLICATION_ENCODE));

		} catch (Exception e) {
			e.printStackTrace();
			return (Hashtable) prop;
		}

		return (Hashtable) prop;

	}
}
