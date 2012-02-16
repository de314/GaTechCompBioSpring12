package scfg.output.ggobi;

import java.util.*;

public class ggobiVariable {
	
	public static enum VariableType {
		realvariable, integervariable, categoricalvariable
	}
	
	private VariableType type;
	private List<String> values;
	private String name;
	private String nickname;
	
	public ggobiVariable(VariableType type, String name) {
		this (type, name, null);
	}
	
	public ggobiVariable(VariableType type, String name, String nickname) {
		this(type, name, nickname, new ArrayList<String>());
	}
	
	public ggobiVariable(VariableType type, String name, String nickname, List<String> values) {
		this.type = type;
		this.name = name;
		this.nickname = nickname;
		this.values = values;
	}
	
	public XMLdiv getXMLdiv() {
		XMLdiv curr = new XMLdiv(type.toString(), -1);
		curr.addValue("name", name);
		if (nickname != null)
			curr.addValue("nickname", nickname);
		return curr;
	}
	
	public String getValue(int index) {
		return values.get(index);
	}
}
