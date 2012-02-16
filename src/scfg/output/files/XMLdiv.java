package scfg.output.files;

import java.util.*;
import java.util.Map.Entry;

public class XMLdiv {

	private String header;
	private int level;
	private Map<String, String> values;
	private StringBuilder content;

	public String getHeader() {
		return header;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getValues(String key) {
		return values.get(key);
	}

	public void addValue(String key, String value) {
		this.values.put(key, value);
	}

	public String getContent() {
		return content.toString();
	}

	public void appendContent(String moreContent) {
		this.content.append(moreContent);
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public XMLdiv(String header, int level) {
		this.header = header;
		this.level = level;
		this.values = new HashMap<String, String>();
		this.content = new StringBuilder();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getOpeningLine()).append("\n");
		sb.append(content).append("\n");
		return sb.append(getClosingLine()).toString();
	}
	
	public String getOpeningLine() {
		StringBuilder sb = new StringBuilder();
		for (int i=0;i<level;i++)
			sb.append("\t");
		sb.append("<").append(header).append(" ");
		for (Entry<String, String> me : values.entrySet())
			sb.append(me.getKey()).append("=\"").append(me.getValue()).append("\" ");
		return sb.deleteCharAt(sb.length()-1).append(">").toString();
	}
	
	public String getClosingLine() {
		StringBuilder sb = new StringBuilder();
		for (int i=0;i<level;i++)
			sb.append("\t");
		return sb.append("</").append(header).append(">").toString();
	}
	
	public String oneLiner() {
		StringBuilder sb = new StringBuilder(getOpeningLine());
		return sb.insert(sb.length()-1," /").toString();
	}
}
