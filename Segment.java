//segment utility class
public class Segment {
	int x1,y1,x2,y2;
	public Segment(int x1, int y1, int x2, int y2)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public boolean ccw(int x1, int y1, int x2, int y2, int x3, int y3)
	{
		return (y3-y1)*(x2-x1)>(y2-y1)*(x3-x1);
	}
	
}
