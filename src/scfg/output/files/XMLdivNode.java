package scfg.output.files;

import java.util.*;

public class XMLdivNode extends XMLdiv {
	
	private Map<String, XMLdivNode> children;
	
	public void addChild(String header) {
		addChild(new XMLdivNode(header, getLevel()+1));
	}
	
	public void addChild(XMLdiv newChild) {
		addChild(new XMLdivNode(newChild.getHeader(), getLevel() + 1));
	}
	
	public void addChild(XMLdivNode newChild) {
		children.put(newChild.getHeader(), newChild);
	}
	
	public XMLdivNode[] getChildren() {
		XMLdivNode[] arr = new XMLdivNode[children.size()];
		int count = 0;
		for (XMLdivNode div : children.values())
			arr[count++] = div;
		return arr;
	}
	
	public XMLdiv getChild(String header) {
		return children.get(header);
	}
	
	public boolean isLeaf() {
		return children.size() == 0;
	}
	
	public int size() {
		return children.size();
	}

	public XMLdivNode(String header, int level) {
		super(header, level);
		children = new HashMap<String, XMLdivNode>();
	}
	
	@Override
	public String toString() {
		for (XMLdivNode child : children.values())
			appendContent(child.toString());
		return super.toString();
	}
}
