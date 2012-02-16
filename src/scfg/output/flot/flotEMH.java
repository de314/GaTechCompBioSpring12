package scfg.output.flot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class flotEMH {

	protected Map<String, List<String>> values;
	
	public flotEMH() {
		values = new HashMap<String, List<String>>();
		values.put("keys", new LinkedList<String>());
	}
	
	public void addValue(String key, String val) {
		if (values.get(key) == null) {
			values.put(key, new LinkedList<String>());
			addKey(key);
		}
		values.get(key).add(val);
	}
	
	public void addKey(String key) {
		if (!values.get("keys").contains(key))
			values.get("keys").add("\""+key+"\"");
	}
	
	public Map<String, List<String>> getSeries() {
		return values;
	}

}
