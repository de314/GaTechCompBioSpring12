package desposito6.datastructures;

import java.util.*;

public class PriorityDeque<T extends Comparable<T>> {

	private Node<T> head;
	private Node<T> tail;
	private int size;
	private boolean allowDuplicates;
	
	public void setAllowDuplicates(boolean allowDuplicates){
		this.allowDuplicates = allowDuplicates;
	}

	public PriorityDeque() {
		this(true);
	}

	public PriorityDeque(boolean allowDuplicates) {
		this.allowDuplicates = allowDuplicates;
	}

	public void insert(T data) {
		if (head == null) {
			head = new Node<T>(data);
			tail = head;
		} else if (head.getData().compareTo(data) > 0) {
			Node<T> temp = new Node<T>(data, head);
			head.setPrev(temp);
			head = temp;
		} else {
			Node<T> temp = new Node<T>(data);
			Node<T> curr = head;
			while (curr.getNext() != null
					&& curr.getNext().compareTo(temp) <= 0)
				curr = curr.getNext();
			if (!allowDuplicates && curr.getData().compareTo(data) == 0)
				return;
			if (curr.getNext() == null) {
				curr.setNext(temp);
				temp.setPrev(curr);
				tail = temp;
			} else {
				curr.getNext().setPrev(temp);
				temp.setNext(curr.getNext());
				curr.setNext(temp);
				temp.setPrev(curr);
			}
		}
		size++;
	}

	public T get(int i) {
		if (i < 0 || i >= size)
			throw new ArrayIndexOutOfBoundsException(i);
		Node<T> curr = head;
		for (int j = 0; j < i && curr != null; j++)
			curr = curr.getNext();
		if (curr == null)
			return null;
		return curr.getData();
	}

	public T getMin() {
		if (head != null)
			return head.getData();
		return null;
	}

	public T getMax() {
		if (tail != null)
			return tail.getData();
		return null;
	}

	public List<T> getMin(int quantity) {
		if (quantity <= 0)
			throw new IllegalArgumentException("" + quantity);
		List<T> list = new LinkedList<T>();
		Node<T> curr = head;
		for (int i = 0; i < quantity && curr != null; i++) {
			list.add(curr.getData());
			curr = curr.getNext();
		}
		return list;
	}

	public List<T> getMax(int quantity) {
		if (quantity <= 0)
			throw new IllegalArgumentException("" + quantity);
		List<T> list = new LinkedList<T>();
		Node<T> curr = tail;
		for (int i = 0; i < quantity && curr != null; i++) {
			list.add(curr.getData());
			curr = curr.getPrev();
		}
		return list;
	}

	public T remove(int i) {
		if (i < 0 || i >= size)
			throw new ArrayIndexOutOfBoundsException(i);
		if (head == null)
			return null;
		if (i == 0) {
			T data = head.getData();
			head = head.getNext();
			if (head != null)
				head.setPrev(null);
			size--;
			if (size <= 1)
				tail = head;
			return data;
		}
		if (i == size - 1) {
			T data = tail.getData();
			tail = tail.getPrev();
			tail.setNext(null);
			size--;
			if (size <= 1)
				head = tail;
			return data;
		}
		Node<T> curr = head;
		for (int j = 0; j < i && curr != null; j++)
			curr = curr.getNext();
		if (curr == null)
			return null;
		if (curr.getNext() != null)
			curr.getNext().setPrev(curr.getPrev());
		curr.getPrev().setNext(curr.getNext());
		T data = curr.getData();
		size--;
		return data;
	}

	public T popMin() {
		return remove(0);
	}

	public T popMax() {
		return remove(size - 1);
	}

	public List<T> popMin(int quantity) {
		if (quantity <= 0)
			throw new IllegalArgumentException("" + quantity);
		List<T> list = new LinkedList<T>();
		for (int i = 0; i < quantity && !isEmpty(); i++)
			list.add(popMin());
		return list;
	}

	public List<T> popMax(int quantity) {
		if (quantity <= 0)
			throw new IllegalArgumentException("" + quantity);
		List<T> list = new LinkedList<T>();
		for (int i = 0; i < quantity && !isEmpty(); i++)
			list.add(popMax());
		return list;
	}

	public void reset() {
		head = null;
		tail = null;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public int size() {
		return size;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		Node<T> temp = head;
		while (temp != null) {
			sb.append(temp.getData()).append(", ");
			temp = temp.getNext();
		}
		if (sb.length() > 1)
			sb.delete(sb.length() - 2, sb.length());
		else
			sb.append(" ");
		sb.append("]");
		return sb.toString();
	}

	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//		PriorityDeque<Integer> d = new PriorityDeque<Integer>();
//		for (int i = 0; i < 20; i++) {
//			int rand = (int) (Math.random() * 100);
//			System.out.println(i + ": " + rand);
//			d.insert(rand);
//		}
//		d.insert(8);
//		d.insert(82);
//		d.insert(28);
//		d.insert(38);
//		d.insert(18);
//		d.insert(106);
//		d.insert(102);
//		d.insert(-1);
//		d.insert(-3);
//		d.insert(16);
//		d.insert(34);
//		d.insert(43);
//		System.out.println(d + "\n");
//
//		System.out.println("-3 = " + d.getMin());
//		System.out.println("-3 = " + d.popMin());
//		System.out.println("-1 = " + d.getMin());
//		System.out.println("-1 = " + d.popMin());
//
//		System.out.println("106 = " + d.getMax());
//		System.out.println("106 = " + d.popMax());
//		System.out.println("102 = " + d.getMax());
//		System.out.println("102 = " + d.popMax());
//
//		System.out.println(d + "\n");
//
//		List<Integer> min = d.getMin(100);
//		System.out.println("min 100: " + min);
//		List<Integer> max = d.getMax(100);
//		System.out.println("max 100: " + max);
//
//		System.out.println(d + "\n");
//
//		min = d.popMin(5);
//		System.out.println("min 5: " + min);
//		max = d.popMax(5);
//		System.out.println("max 5: " + max);
//
//		System.out.println(d + "\n");
//
//		int i = 1;
//		while (!d.isEmpty()) {
//			System.out.println("Set " + (i++) + ":" + d.popMin(3));
//		}
//
//		d = new PriorityDeque<Integer>(false);
//		d.insert(5);
//		d.insert(5);
//		d.insert(5);
//		d.insert(5);
//		d.insert(5);
//
//		System.out.println(d);
//
//		d = new PriorityDeque<Integer>(true);
//		d.insert(5);
//		d.insert(5);
//		d.insert(5);
//		d.insert(5);
//		d.insert(5);
//
//		System.out.println(d);
//
//		d = new PriorityDeque<Integer>();
//		d.insert(5);
//		d.insert(5);
//		d.insert(5);
//		d.insert(5);
//		d.insert(5);
//
//		System.out.println(d);
//
//		d.reset();
//		System.out.println(d);
//	}

}
