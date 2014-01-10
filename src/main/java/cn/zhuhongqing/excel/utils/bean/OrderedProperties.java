package cn.zhuhongqing.excel.utils.bean;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

/**
 * OrderedProperties
 * 
 * @author Mr.Yi
 * @date 2013-11-13
 */
public class OrderedProperties extends Properties {

	private static final long serialVersionUID = -4627607243846121965L;

	private final LinkedHashSet<Object> keys = new LinkedHashSet<Object>();

	public Enumeration<Object> keys() {
		return Collections.<Object> enumeration(keys);
	}

	public Object put(Object key, Object value) {
		keys.add(key);
		return super.put(key, value);
	}

	public Set<Object> keySet() {
		return keys;
	}

	@Override
	public synchronized Object remove(Object key) {
		keys.remove(key);
		return super.remove(key);
	}

	public Set<String> stringPropertyNames() {
		Set<String> set = new LinkedHashSet<String>();

		for (Object key : this.keys) {
			set.add((String) key);
		}

		return set;
	}
}