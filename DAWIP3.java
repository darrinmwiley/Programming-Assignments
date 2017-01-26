import java.util.Random;
import java.util.*;
//union find data structure, used for maze generation
public class DAWIP3 {
	
	class UnionFind
	{
		int finds = 0;
		double totalProbes = 0;
		int currentProbeLength = 0;
		int calls = 0;
		int[] ints;
		public UnionFind(int N)
		{
			ints = new int[N];
			Arrays.fill(ints, -1);
		}
		
		boolean union(int x, int y)
		{
			int a = find(x);
			int b = find(y);
			if(a==b)
				return false;
			if(ints[a]<=ints[b])
			{
				ints[a]+=ints[b];		//combine sets and increase size
				ints[b] = a;	
			}
			else{
				ints[b]+=ints[a];
				ints[a] = b;
			}
			return true;
		}
		
		public int find(int y)
		{
			if(ints[y]<0)
			{
				totalProbes+=currentProbeLength;	//compress path and update stats
				currentProbeLength = 0;
				finds++;
				return y;
			}	
			currentProbeLength++;
			System.out.println("length at "+y+" is "+(totalProbes+currentProbeLength));
			return ints[y] = find(ints[y]);
		}
		
		public int sets()
		{
			int x = 0;
			for(int i:ints)
				if(i<0)
					x++;
			return x;
		}
		
		public void clear()
		{
			Arrays.fill(ints, -1);
		}
		
		void printArray()
		{
			System.out.println(Arrays.toString(ints));
		}
		
		void printCon()
		{
			for(int i = 0;i<ints.length;i++)			//finds vertex that is parent
			{
				if(ints[i]<0)
				{
					System.out.print(i+" ");
					for(int j = i+1;j<ints.length;j++)	//finds every vertex in set
						if(find(j)==i)					
							System.out.print(j+" ");
					System.out.println();
				}
			}
		}
		
		public void makeMaze(int w, int d, int h)
		{
			int edges = 0;
			int[][] weights = new int[w*d*h][w*d*h];
			for(int[] x:weights)
				Arrays.fill(x,-1);
			UnionFind set = new UnionFind(w*d*h);
			while(edges!=w*d*h-1)
			{
				int i = (int)(Math.random()*w*d*h);
				int vw = i%w;
				int vd = i/w%d;
				int vh = i/(d*w);
				int direction = (int)(Math.random()*6);
				switch(direction)
				{
				case 0://north
					if(vd!=0&&set.union(i,i-w))
					{
						weights[i][i-w] = (int)(Math.random()*20+1);
						edges++;
					}
					break;
				case 1://south
					if(vd!=d-1&&set.union(i,i+w))
					{
						weights[i][i+w] = (int)(Math.random()*20+1);
						edges++;
					}
					break;
				case 2://west
					if(vw!=0&&set.union(i,i-1))
					{
						weights[i][i-1] = (int)(Math.random()*20+1);
						edges++;
					}
					break;
				case 3://east
					if(vw!=w-1&&set.union(i,i+1))
					{
						weights[i][i+1] = (int)(Math.random()*20+1);
						edges++;
					}
					break;
				case 4://up
					if(vh!=0&&set.union(i,i-w*d))
					{
						weights[i][i-w*d] = (int)(Math.random()*20+1);
						edges++;
					}
					break;
				case 5://down
					if(vh!=h-1&&set.union(i,i+w*d))
					{
						weights[i][i+w*d] = (int)(Math.random()*20+1);
						edges++;
					}
				}
			}
			for(int i = 0;i<weights.length;i++)
			{
				boolean flag = false;
				for(int j = i+1;j<weights.length;j++)
					if(weights[i][j]>0||weights[j][i]>0) //decided if number needs to be printed in adjacency list or skipped
					{
						flag = true;					
					}		
				if(flag)
				{
					System.out.print(i+" ");
					for(int j = i+1;j<weights.length;j++)
						if(weights[i][j]>0||weights[j][i]>0)
							System.out.print(j+" ");			//prints vertex
					for(int j = i+1;j<weights.length;j++)
						if(weights[i][j]>0||weights[j][i]>0)
							System.out.print(Math.max(weights[i][j],weights[j][i])+" "); //prints weight
					System.out.println();
				}
			}
				
					
		}
		
		public void printStats(){
			System.out.printf("Number of disjoint sets remaining = %d%nMean path length of all find operations = %.2f",sets(),(totalProbes)/finds);
			System.out.println(finds);
		}
	}

	public static void main(String[] args)
	{
		Scanner file = new Scanner(System.in);
		int N = file.nextInt();file.nextLine();
		UnionFind set = new DAWIP3().new UnionFind(N);
		while(file.hasNext())
		{
			String[] line = file.nextLine().split(" ");
			if(line[0].equals("u"))
			{
				if(set.union(Integer.parseInt(line[1]),Integer.parseInt(line[2])))
				{
					System.out.println("True");
				}
				else{
					System.out.println("False");
				}
			}
			if(line[0].equals("f"))
			{
				System.out.println(set.find(Integer.parseInt(line[1])));
			}
			if(line[0].equals("p"))
			{
				set.printArray();
			}
			if(line[0].equals("c"))
			{
				set.printCon();
			}
			if(line[0].equals("S"))
			{
				set.printStats();
			}
			if(line[0].equals("m"))
			{
				set.makeMaze(Integer.parseInt(line[1]),Integer.parseInt(line[2]),Integer.parseInt(line[3]));
			}
			if(line[0].equals("e"))
				return;
		}
	}
}

/*
100
u 56 27
u 23 41
u 1 2
u 3 4
u 5 6
u 8 9
u 2 3
u 6 8
u 41 27
f 17
f 5
f 2
f 6
f 1
f 5
f 9 1
u 10 11
u 11 12
u 13 14
u 10 12
u 12 14
f 10
f 14
f 15
f 15
p
c
S
m 8 9 10
e
*/
