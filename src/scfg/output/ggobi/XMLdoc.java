package scfg.output.ggobi;

import scfg.output.Display;
import java.util.*;

public class XMLdoc {
	
	private XMLdivNode root;
	private Display output;
	
	public XMLdoc(String filename) {
		this(new Display(filename, null, false));
	}
	
	public XMLdoc(Display output) {
		this.output = output;
	}
	
	@Override
	public String toString() {
		return root == null ? "" : root.toString();
	}
	
	public Iterator<XMLdiv> getPreorderTraversal() {
		List<XMLdiv> divs = new LinkedList<XMLdiv>();
		if (root != null) {
			Queue<XMLdivNode> levelQ = new LinkedList<XMLdivNode>();
			levelQ.add(root);
			while (!levelQ.isEmpty()) {
				XMLdivNode curr = levelQ.poll();
				divs.add(curr);
				if (!curr.isLeaf()) {
					XMLdivNode[] arr = curr.getChildren();
					for (int i=0;i<arr.length;i++) 
						levelQ.add(arr[i]);
				}
			}
		}
		return divs.iterator();
	}
	
	public Iterator<XMLdiv> getPostTraversal() {
		// TODO
		return null;
	}

}
