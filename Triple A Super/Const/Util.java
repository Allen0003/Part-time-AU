package Const;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Util {

	public static Map<String, Object> showFields(Object o) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (Field field : o.getClass().getDeclaredFields()) {
			for (Method method : o.getClass().getMethods()) {
				if ((method.getName().startsWith("get"))
						&& (method.getName().length() == (field.getName().length() + 3))) {
					if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
						try {
							method.invoke(o);
							map.put(field.getName(), method.invoke(o));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return map;
	}
}
