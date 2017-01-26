import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;
//Union Find data structure used to run Kruskals algorithm to produce a min spanning tree, as well as floyd warshall implementation and my own experimental graph coloring algorithms
public class DAWIP4 {
	
	class UnionFind
	{
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
				ints[a]+=ints[b];
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
				return y;
			return ints[y] = find(ints[y]);
		}
	}
	
	private class Edge implements Comparable<Edge>
	{
		int a,b;
		double cost;
		
		public Edge(int x, int y, double z)
		{
			a = x;
			b = y;
			cost = z;
		}
		
		@Override
		public int compareTo(Edge arg0) {
			if(cost<arg0.cost)
				return -1;
			if(cost>arg0.cost)
				return 1;
			return 0;
		}
		
		public String toString()
		{
			return String.format("%d %d %.2f",a+1,b+1,cost);
		}
		
	}
	
	public double dist(double x1, double y1, double x2, double y2)//for easy distance
	{
		return (x1-x2)*(x1-x2)+(y1-y2)*(y1-y2);
	}
	
	public V[] makeV(Edge[] edges, int N)//makes list of vertices/connections for easy coloring
	{
		V[] ret = new V[N];
		int[] sizes = new int[N];
		for(Edge e:edges)
		{
			sizes[e.a]++;
			sizes[e.b]++;
		}
		for(int i = 0;i<sizes.length;i++)
		{
			ret[i] = new V(i,sizes[i]);
		}
		for(Edge e:edges)
		{
			ret[e.a].add(e.b);
			ret[e.b].add(e.a);
		}
		return ret;
	}
	
	public void run() throws NumberFormatException, IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int N = Integer.parseInt(br.readLine());
		double[] x = new double[N];
		double[] y = new double[N];
		double[][] dist = new double[N][N];
		int[][] hops = new int[N][N];
		StringTokenizer st;
		for(int i = 0;i<N;i++)
		{
			st = new StringTokenizer(br.readLine());
			x[i] = Double.parseDouble(st.nextToken());
			y[i] = Double.parseDouble(st.nextToken());
		}
		double R = Double.parseDouble(br.readLine());
		double R2 = R*R;
		int E = 0;
		for(int i = 0;i<N;i++)
		{
			for(int j =0;j<i;j++)
			{
				double d = dist(x[i],y[i],x[j],y[j]);
				if(i!=j&&d<=R2)
				{
					dist[i][j] = dist[j][i] = Math.sqrt(d);
					hops[i][j] = hops[j][i] = 1;
					E++;
				}else
					dist[i][j] = dist[j][i] = Double.POSITIVE_INFINITY;
			}
		}
		Edge[] edges = new Edge[E];
		int c =0;
		for(int i = 0;i<N;i++)
		{
			for(int j = 0;j<i;j++)
			{
				if(hops[j][i]!=0)
					edges[c++] = new Edge(j,i,dist[i][j]);
			}
		}
		Arrays.sort(edges);//sort edges by distances
		UnionFind uf = new UnionFind(N);
		double len = 0;
		for(int i = 0;i<E;i++)
		{
			Edge ed = edges[i];//add best edge to tree
			if(uf.union(ed.a,ed.b))
			{
				System.out.println(ed);
				len+=ed.cost;
			}
		}
		System.out.printf("%.2f%n",len);
		int[][] fw = new int[N][N];//floyd warshall matrix
		int[][] next = new int[N][N];//stores predecessors
		for(int i = 0;i<next.length;i++)
		{
			for(int j = 0;j<next.length;j++)
			{
				next[i][j] = j;
			}
		}
		for(int i = 0;i<N;i++)//floyd warshall array pre fill
		{
			for(int j = 0;j<N;j++)
			{
				if(hops[i][j]==1)
					fw[i][j] = 1;
				else if(i==j)
					fw[i][j] = 0;
				else
					fw[i][j] = Integer.MAX_VALUE/4;
			}
		}
		for(int k = 0;k<N;k++)//floyd warshall
			for(int i = 0;i<N;i++)
				for(int j = 0;j<N;j++)
					if(fw[i][j]>fw[i][k]+fw[k][j])
					{
						fw[i][j] = fw[i][k]+fw[k][j];
						next[i][j] = next[i][k];
					}
		for(int i = 1;i<N;i++)//trace back paths
		{
			int current = 0;
			while(current!=i)
			{
				System.out.print(current+1+" ");
				current = next[current][i];
			}
			System.out.println(i+1+" "+fw[0][i]);
		}
		int worst = 0;//find worst path
		for(int[] in:fw)
			for(int i:in)
				worst = Math.max(worst,i);
		System.out.println(worst);
		System.out.println(color(edges,N));
	}
	
	public int color(Edge[] edges, int N)
	{
		V[] v = makeV(edges,N);
		int min = maxDegreeColor(v);
		// I run each coloring algorithm 1000 times and take the best answer. they are random greedy algorithms, so i figure one will produce a pretty good answer. If not, i fall back on the max degree upper bound.
		for(int i =0 ;i<1000;i++)
			min = Math.min(randomColor(v),min);
		for(int i = 0;i<1000;i++)
			min = Math.min(randomNeighborColor(v,new int[v.length],(int)(Math.random()*v.length),0),min);
		return min;
	}
	
	public int randomNeighborColor(V[] vertices,int[] colors, int next, int maxColor)//starts at a random point and colors each neighbor randomly. works better on bipartite graphs where randomcolor can perform poorly
	{
		int color = 1;
		boolean repeat = true;
		while(repeat)
		{
			repeat = false;
			for(int j:vertices[next].con)
			{
				if(colors[j]==color)
				{
					color++;
					repeat = true;
					break;
				}
			}
		}
		colors[next] = color;
		maxColor = Math.max(color,maxColor);
		int uncoloredNeighbors = 0;
		for(int i = 0;i<vertices[next].con.length;i++)
		{
			if(colors[vertices[next].con[i]]==0)
				uncoloredNeighbors++;
		}
		while(hasUncoloredNeighbors(vertices[next],colors))
		{
			int nextnext = vertices[next].con[(int)(Math.random()*vertices[next].con.length)];
			while(colors[nextnext]!=0)
			{
				nextnext = vertices[next].con[(int)(Math.random()*vertices[next].con.length)];
			}
			maxColor = Math.max(maxColor,randomNeighborColor(vertices,colors,nextnext,maxColor));
		}
		return maxColor;
	}
	
	public boolean hasUncoloredNeighbors(V v, int[] colors)//convenience
	{
		for(int x:v.con)
		{
			if(colors[x]==0)
				return true;
		}
		return false;
	}
	
	public int randomColor(V[] vertices)// colors a random node the smallest color until all are colored
	{
		int maxColor = 1;
		int[] colors = new int[vertices.length];
		for(int i = 0;i<vertices.length;i++)
		{
			int next = (int)(Math.random()*colors.length);
			while(colors[next]!=0)
			{
				next = (int)(Math.random()*colors.length);
			}
			int color = 1;
			boolean repeat = true;
			while(repeat)
			{
				repeat = false;
				for(int j:vertices[next].con)
				{
					if(colors[j]==color)
					{
						color++;
						repeat = true;
						break;
					}
				}
			}
			colors[next] = color;
			maxColor = Math.max(maxColor,color);
		}
		return maxColor;
	}
	
	public int maxDegreeColor(V[] vertices)//simple max degree upper bound
	{
		int max = 0;
		for(V v:vertices)
			if(v.con.length>max)
				max = v.con.length;
		return max+1;
	}

	private class V
	{
		int id;
		int[] con;
		int spot;
		public V(int id, int conLen)
		{
			spot = 0;
			this.id = id;
			con = new int[conLen];
		}
		public void add(int N)
		{
			con[spot++] = N;
		}
	}
	
	public static void main(String[] args) throws NumberFormatException, IOException
	{
		new DAWIP4().run();
	}	
}