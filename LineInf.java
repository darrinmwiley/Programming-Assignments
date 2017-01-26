import java.util.Arrays;
//line utility class
public class LineInf {
	double A,B,C;//want line in form Ax+By = C
	
	public LineInf(double x1, double y1, double x2, double y2)
	{
		A = y2-y1;
		B = x1-x2;
		C = A*x1+B*y1;
	}
	
	private LineInf(double A, double B, double C)
	{
		this.A = A;
		this.B = B;
		this.C = C;
	}
	
	public double det(LineInf li)
	{
		return A*li.B-li.A*B;
	}
	
	public double[] intersection(LineInf li)
	{
		double det = det(li);
		if(det==0) return null; //zero or infinite solutions
		double x = (li.B*C-B*li.C)/det;
		double y = (A*li.C-li.A*C)/det;
		return new double[]{x,y};
	}
	
	public double dist(double x, double y)
	{
		LineInf perp = new LineInf(-B,A,-B*x+A*y);
		double[] intersect = intersection(perp);
		return Math.sqrt(Math.pow(x-intersect[0],2)+Math.pow(y-intersect[1],2));
	}
	
	public static double[] segInt(double x1, double y1, double x2, double y2,
							double x3, double y3, double x4, double y4)
	{
		LineInf a = new LineInf(x1,y1,x2,y2);
		LineInf b = new LineInf(x3,y3,x4,y4);
		double[] z = a.intersection(b);
		double ax = Math.sqrt(Math.pow(x1-z[0], 2)+Math.pow(y1-z[1], 2));
		double xb = Math.sqrt(Math.pow(x2-z[0], 2)+Math.pow(y2-z[1], 2));
		double ab = Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2));
		if(Math.abs(ax+xb-ab)<.00001)
			return z;
		return null;
	}
}
