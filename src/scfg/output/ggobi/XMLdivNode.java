package scfg.output.ggobi;

import java.util.*;

public class XMLdivNode extends XMLdiv {
	
	private List<XMLdivNode> children;
	
	public void addChild(String header) {
		addChild(new XMLdivNode(header, getLevel()+1));
	}
	
	public void addChild(XMLdiv newChild) {
		addChild(new XMLdivNode(newChild.getHeader(), getLevel() + 1, newChild.values, content.toString()));
	}
	
	public void addChild(XMLdivNode newChild) {
		children.add(newChild);
		newChild.setLevel(getLevel()+1);
	}
	
	public XMLdivNode[] getChildren() {
		XMLdivNode[] arr = new XMLdivNode[children.size()];
		int count = 0;
		for (XMLdivNode div : children)
			arr[count++] = div;
		return arr;
	}
	
//	public XMLdiv getChild(String header) {
//		return children.get(header);
//	}
	
	public boolean isLeaf() {
		return children.size() == 0;
	}
	
	public int size() {
		return children.size();
	}

	public XMLdivNode(String header, int level) {
		super(header, level);
		children = new ArrayList<XMLdivNode>();
	}

	public XMLdivNode(String header, int level, Map<String, String> values) {
		super(header, level, values);
		children = new ArrayList<XMLdivNode>();
	}

	public XMLdivNode(String header, int level, Map<String, String> values, String content) {
		super(header, level, values, content);
		children = new ArrayList<XMLdivNode>();
	}
	
	@Override
	public String toString() {
		for (XMLdivNode child : children)
			appendContent(child.toString());
		return (content.length() == 0? super.oneLiner() : super.toString()) + "\n";
	}
}
