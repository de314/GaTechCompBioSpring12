package desposito6.datastructures;

public class Node<T extends Comparable<T>> implements Comparable<Node<T>> {
	
	private Node<T> next;
	private Node<T> prev;
	private T data;
	
	public Node<T> getNext() {
		return next;
	}
	public void setNext(Node<T> next) {
		this.next = next;
	}
	public Node<T> getPrev() {
		return prev;
	}
	public void setPrev(Node<T> prev) {
		this.prev = prev;
	}
	
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public Node(){
		this(null, null, null);
	}
	
	public Node(T data){
		this(data, null, null);
	}
	
	public Node(T data, Node<T> next){
		this(data, next, null);
	}
	
	public Node(T data, Node<T> next, Node<T> prev){
		this.data = data;
		this.next = next;
		this.prev = prev;
	}

	@Override
	public int compareTo(Node<T> that){
		if(this.data == null || that.data == null){
			if(this.data == that.data)
				return 0;
			if(this.data == null)
				return 1;
			return -1;
		}
		return this.data.compareTo(that.getData());
	}
	
	@Override
	public String toString(){
		return data.toString();
	}
}
