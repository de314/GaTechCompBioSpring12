package scfg.output.flot;

import java.util.*;

public class flotSingle extends flotEMH {
	
	protected String label;
	protected boolean addedPair;
	protected boolean addedKey;
	
	public String getLabel() {
		return label;
	}
	
	public flotSingle(String label) {
		super();
		this.label = label;
		values.put("predefinedSeries", new LinkedList<String>());
	}
	
	public void addPair(String a, String b) {
		values.get("predefinedSeries").add(formatPair(a, b));
	}
	
	private String formatPair(String a, String b) {
//		StringBuilder sb = new StringBuilder("[ \"");
		StringBuilder sb = new StringBuilder("[ ");
//		sb.append(a).append("\", \"").append(b).append("\" ]");
		sb.append(a).append(", ").append(b).append(" ]");
		return sb.toString();
	}
}
