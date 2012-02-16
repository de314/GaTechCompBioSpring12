package scfg.output.flot;

import java.util.*;

public class flotInt {
	
	private Map<String, Map<String, List<String>>> values;
	
	public flotInt() {
		values = new HashMap<String, Map<String, List<String>>>();
		values.put("keys", new HashMap<String, List<String>>());
		values.get("keys").put("keys", new LinkedList<String>());
	}
	
	public void addValue(String key, int subset, String val) {
		addValue(key, subset == 0 ? "easy" : subset == 1 ? "medium" : "hard", val);
	}
	
	public void addValue(String key, String subset, String val) {
		if (values.get(key) == null) {
			values.put(key, new HashMap<String, List<String>>());
		}
		if (values.get(key).get(subset) == null)
			values.get(key).put(subset, new LinkedList<String>());
		values.get(key).get(subset).add(val);
	}
	
	public void addKey(String key) {
		if (!values.get("keys").get("keys").contains(key)) {
			if (key.charAt(0) != '"')
				key = "\"" + key;
			if (key.charAt(key.length()-1) != '"')
				key += "\"";
			values.get("keys").get("keys").add(key);
		}
	}
	
	public Map<String, Map<String, List<String>>> getSeries() {
		return values;
	}
}
