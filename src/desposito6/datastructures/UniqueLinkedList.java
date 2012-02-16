package desposito6.datastructures;

import java.util.LinkedList;

public class UniqueLinkedList<E> extends LinkedList<E> {
	
	@Override
	public boolean add(E object) {
		boolean containsObj = this.contains(object); 
		if (!containsObj)
			super.add(object);
		return containsObj;		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
