package QuickBase;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class ObjectUtils {

	public Map<String, Object> getFieldNamesAndVals(Object obj) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Class<? extends Object> c1 = obj.getClass();
			Field[] fields = c1.getDeclaredFields();
			for (Field field : fields) {
				String name = field.getName();
				if (Modifier.isPublic(field.getModifiers())) {
					Object value = field.get(obj);
					map.put(name, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public static void main(String[] args) throws Exception {
		Dummy dummy = new Dummy();
		dummy.setValue2(99);
		dummy.setValue1("qoo");
		System.out.println(new ObjectUtils().getFieldNamesAndVals(dummy));
	}
}

class Dummy {
	public String first;
	public int value2;

	public String getValue1() {
		return first;
	}

	public void setValue1(String value1) {
		this.first = value1;
	}

	public int getValue2() {
		return value2;
	}

	public void setValue2(int value2) {
		this.value2 = value2;
	}

}
