import java.util.Random;
import java.util.*;
//trie data structure
public class DAWIP2 {
	
	class Trie
	{
		Node head;
		
		public Trie()
		{
			head = new Node();
		}
		
		public boolean insert(String s)
		{
			Node current = head;
			while(!s.isEmpty())
			{
				if(current.children[s.charAt(0)-97] == null)		//check if child needs to be made
				{
					current.children[s.charAt(0)-97] = new Node();	//make child
					current.outDegree++;							//update outDegree
				}
				current = current.children[s.charAt(0)-97];			//go to next child
				s = s.substring(1);									//chop first letter off string
			}
			if(current.terminal)
				return false;
			return current.terminal = true;
		}
		
		public boolean delete(String s)//checks to see if word is present, and makes call to private delete funtion if so
		{
			if(isPresent(s))
			{
				delete(head,s);
				return true;
			}
			return false;
		}
		
		private boolean delete(Node n, String s)		//recurses down,flips terminal, and then returns true if it needs to be deleted by its parent
		{
			if(s.isEmpty())								//means we found the terminal node of the string
			{
				n.terminal = false;
				return n.outDegree==0;
			}
			if(delete(n.children[s.charAt(0)-97],s.substring(1)))//true if child needs deletion
			{
				n.children[s.charAt(0)-97] = null;		//delete child
				n.outDegree--;							//decrement outDegree if child gets deleted
				return n.outDegree == 0&&!n.terminal;	//delete me if i have no children and i'm not terminal
			}
			return false;//means we dont need to be deleted
		}
		
		public boolean isPresent(String s)
		{
			Node current = head;
			while(!s.isEmpty())									//stops when end of word is found
			{
				if(current.children[s.charAt(0)-97] == null)	//if the node to go to next does not exist, then return false
					return false;
				else
					current = current.children[s.charAt(0)-97];	//else keep going
				s = s.substring(1);								//chop first letter off string
			}
			return current.terminal;							//if we find the right node, return that it's terminal
		}
		
		public int membership()
		{
			return membership(head);
		}
		
		private int membership(Node n)
		{
			int ret = 0;				
			if(n.terminal)				//terminal means theres another member
				ret++;
			for(Node x:n.children)		//go through children
				if(x!=null)				
					ret+=membership(x);	//add X's children members
			return ret;					//return sum
		}
		
		public void listAll()
		{
			listAll(head,"");
		}
		
		private void listAll(Node current,String s)
		{
			if(current.terminal)									
				System.out.println(s);								//print when things are terminal
			for(int i = 0;i<26;i++)									//go through children
				if(current.children[i]!=null)						
					listAll(current.children[i],s+(char)(i+97));	//make a recursive call to the subtree
		}
	}
	
	class Node{
		boolean terminal;
		int outDegree;
		Node[] children;
		public Node()
		{
			children = new Node[26];
		}
	}

	public static void main(String[] args)
	{
		DAWIP2 asdf = new DAWIP2();
		Trie tree = asdf.new Trie();
		Scanner file = new Scanner(System.in);
		while(true)
		{
			String command = file.next();
			if(command.equals("A"))
			{
				if(tree.insert(file.next()))
					System.out.println("Word inserted");
				else
					System.out.println("Word already exists");
			}
			if(command.equals("M"))
			{
				System.out.println("Membership is "+tree.membership());
			}
			if(command.equals("D"))
			{
				if(tree.delete(file.next()))
				{
					System.out.println("Word deleted");
				}else
					System.out.println("Word not present");
			}
			if(command.equals("C"))
			{
				String[] strs = file.nextLine().trim().split(" ");
				for(String s:strs)
					if(!tree.isPresent(s))
						System.out.println("Spelling mistake "+s);
			}
			if(command.equals("L"))
			{
				tree.listAll();
			}
			if(command.equals("S"))
			{
				if(tree.isPresent(file.next()))
				{
					System.out.println("Word found");
				}else
					System.out.println("Word not present");
			}
			if(command.equals("E"))
			{
				break;
			}
		}
	}
}
/*
A ant
A goat
A frog
A art
A goad
A antler
A go
A from
A part
A past
A arts
A part
A frond
A fries
A text
A message
A mess
A mean
2
A goal
A interpretation
A coat
M
D art
D art
D mess
S art
M
S antler
S part
S text
S freis
C pert post past text
C coat frond interpretacion
C anteler massage cat mouse goal
L
E
 */

	


