//LinkedList and Node Data Structure
public class Homework {

}
class Node{
	private Node next;
	private int key;
	
	public Node(Node nxt, int keyValue){
		next = nxt;
		key = keyValue;
	}
	
	public Node getNext()
	{
		return next;
	}
	
	public int getKey()
	{
		return key;
	}
	
	public void putNext(Node nxt)
	{
		next = nxt;
	}
}
class List{
	private Node head;
	
	public boolean exists(int ky)
	{
		Node current = head;
		while(current!=null)
		{
			if(current.getKey()==ky)
				return true;
		}
		return false;
	}
	
	public void insertAtHead(int ky)
	{
		Node newHead = new Node(null,ky);
		newHead.putNext(head);
		head = newHead;
	}
	
	public void insertAtTail(int ky)
	{
		Node newTail = new Node(null,ky);
		if(head==null)
		{
			head = newTail; 
		}
		Node current = head;
		while(current.getNext()!=null)
			current = current.getNext();
		current.putNext(newTail);
	}
	
	public int removeFromHead()
	{
		if(head==null)
			return -1;
		Node h = head;
		head = h.getNext();
		h.putNext(null);
		return h.getKey();
	}
	
	public void delete(int ky, Node x)
	{
		Node current = head;
		while(current!=null&&current.getNext()!=null)
		{
			if(current.getNext().getKey()==ky)
			{
				Node toDelete = current.getNext();
				current.putNext(toDelete.getNext());
				toDelete.putNext(null);
				return;
			}
		}
	}
	
	public int maxElement()
	{
		if(head==null)
			return -1;
		return maxElement(head);
	}
	
	private int maxElement(Node current)
	{
		if(current.getNext()==null)
			return current.getKey();
		return Math.max(current.getKey(),maxElement(current.getNext()));
	}
	
	public int minElement()
	{
		if(head==null)
			return -1;
		return minElement(head);
	}
	
	private int minElement(Node current)
	{
		if(current.getNext()==null)
			return current.getKey();
		return Math.min(current.getKey(),minElement(current.getNext()));
	}
	
	public int sum()
	{
		if(head==null)
			return 0;
		return sum(head);
	}
	
	private int sum(Node current)
	{
		if(current.getNext()==null)
			return current.getKey();
		return current.getKey()+sum(current.getNext());
	}
	
	public int length()
	{
		return length(head);
	}
	
	public int length(Node current)
	{
		if(current == null)
			return 0;
		return length(current.getNext())+1;
	}
	
	public void recursiveDelete(int ky)
	{
		if(head.getKey()==ky)
			removeFromHead();
		recursiveDelete(ky,head);
	}
	
	private Node recursiveDelete(int ky, Node current)
	{
		if(current.getNext()==null)
			return null;
		if(current.getNext().getKey()==ky)
		{
			Node toDelete = current.getNext();
			current.putNext(toDelete.getNext());
			toDelete.putNext(null);
			return toDelete;
		}else
			return recursiveDelete(ky,current.getNext());
	}
}