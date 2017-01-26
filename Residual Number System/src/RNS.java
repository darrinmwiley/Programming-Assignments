import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;


public class RNS extends JFrame implements KeyListener,ActionListener{

	private long[] moduli;
	private long[] bases;
	private long range;
	private int maxColumns;
	private Stack<String> stack;
	JTextArea textArea;
	String currentText;
	boolean moduliChanged;
	public RNS()
	{
		super("RNS Calculator");
		initComponents();
		setModuli(new long[]{8,7,5,3});
		stack = new Stack<String>();
		currentText = "";
		this.setFocusable(true);
		addKeyListener(this);
		this.setResizable(false);
		setSize(230,350);
		setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void initComponents()
	{
		JPanel buttonPanel = new JPanel();
		JPanel scrollPanel = new JPanel(new BorderLayout());
		scrollPanel.setBounds(5,5,215,76);
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setBounds(5,5,215,76);
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(scrollPanel.getBounds());
		textArea.setBounds(scroll.getBounds());
		scrollPanel.add(scroll,BorderLayout.CENTER);
		buttonPanel.add(scrollPanel);
		buttonPanel.setLayout(null);
		String[] buttonNames = "< C +/- + 7 8 9 - 4 5 6 * 1 2 3 0 M =".split(" ");
		JButton[] buttons = new JButton[buttonNames.length];
		int[] buttonX = new int[]{5,60,115,170,5,60,115,170,5,60,115,170,5,60,115,5,115,170};
		int[] buttonY = new int[]{86,86,86,86,133,133,133,133,180,180,180,180,227,227,227,274,274,227};
		for(int i = 0;i<buttons.length;i++){
			buttons[i] = new JButton(buttonNames[i]);
			buttons[i].setBounds(buttonX[i], buttonY[i], 50, 40);
			buttons[i].setActionCommand(buttonNames[i]);
			buttons[i].addActionListener(this);
		}
		buttons[15].setBounds(buttonX[15],buttonY[15],107,40);
		buttons[17].setBounds(buttonX[17],buttonY[17],50,87);
		for(JButton b:buttons)
			buttonPanel.add(b);
		add(buttonPanel);
	}
	
	public long[] multiply(long[] a, long[] b)
	{
		long[] c = new long[a.length];
		for(int i = 0;i<a.length;i++)
			c[i] = (a[i]*b[i])%moduli[i];
		return c;
	}
	
	public long[] add(long[] a, long[] b)
	{
		long[] c = new long[a.length];
		for(int i = 0;i<a.length;i++)
			c[i] = (a[i]+b[i])%moduli[i];
		return c;
	}
	
	public long[] subtract(long[] a, long[] b)
	{
		long[] c = new long[a.length];
		for(int i = 0;i<a.length;i++)
			c[i] = (a[i]-b[i]+moduli[i])%moduli[i];
		return c;
	}
	
	public long[] toRNS(long l)
	{
		while(l<0)
			l+=range;
		long[] ans = new long[moduli.length];
		for(int i = 0;i<moduli.length;i++)
			ans[i] = l%moduli[i];
		return ans;
	}
	
	public long toStandard(long[] longs)
	{
		long sum = 0;
		for(int i = 0;i<longs.length;i++)
			sum+=bases[i]*longs[i];
		sum%=range;
		if(sum>(range-1)/2)
			return sum-range;
		return sum;
	}
	
	public void setModuli(long[] moduli)
	{
		maxColumns = 0;
		this.moduli = moduli;
		bases = new long[moduli.length];
		for(int i = 0;i<moduli.length;i++)
		{
			maxColumns = Math.max(maxColumns,(""+moduli[i]).length());
			int product = 1;
			for(int j = 0;j<moduli.length;j++)
				if(i!=j)
					product*=moduli[j];
			for(int j = 1;j<moduli[i];j++)
			{
				if(product*j%moduli[i]==1)
				{
					bases[i] = product*j;
					break;
				}
			}
		}
		range = lcm(moduli);
		System.out.println(Arrays.toString(moduli));
		System.out.println(lcm(moduli));
		textArea.setText("The range is now from "+(-range/2)+" to "+(range-1)/2);
		moduliChanged = true;
	}
	
	public long lcm(long[] list)
	{
		long lcm = 1;
		for(long l:list)
			lcm = lcm*l/gcf(lcm,l);
		return lcm;
	}
	
	public long gcf(long a, long b)
	{
		return new BigInteger(a+"").gcd(new BigInteger(b+"")).longValue();
	}
	
	public static void main(String[] args)
	{
		RNS rns = new RNS();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		buttonPressed(arg0.getActionCommand());
	}
	
	public void buttonPressed(String command)
	{
		if(command.matches("\\d"))
		{
			if(moduliChanged){
				textArea.setText("");
				moduliChanged = false;
			}
				
			textArea.append(command);
			currentText+=command;
		}
		if(command.equals("backspace")||command.equals("<"))
		{
			String text = textArea.getText();
			if(!currentText.isEmpty()){
				textArea.setText(text.substring(0,text.length()-1));
				currentText = currentText.substring(0,currentText.length()-1);
			}	
		}
		if(command.equals("+")&&!currentText.isEmpty())
		{
			if(!stack.isEmpty())
			{
				evaluate();
			}
			stack.add(currentText);
			stack.add("+");
			textArea.append(" + ");
			currentText = "";
			
		}
		if(command.equals("-")&&!currentText.isEmpty())
		{
			if(!stack.isEmpty())
			{
				evaluate();
			}
			stack.add(currentText);
			stack.add("-");
			textArea.append(" - ");
			currentText = "";
			
		}
		if(command.equals("*")&&!currentText.isEmpty())
		{
			if(!stack.isEmpty())
			{
				evaluateToPlus();
			}
			textArea.append(" * ");
			stack.add(currentText);
			stack.add("*");
			currentText = "";
		}
		if(command.equalsIgnoreCase("C"))
		{
			stack.clear();
			textArea.setText("");
			currentText = "";
		}
		if(command.equals("+/-")&&!currentText.isEmpty())
		{
			if(!currentText.startsWith("-"))
			{
				textArea.setText(textArea.getText().replaceAll("(\\d+)$","-$1"));
				currentText = "-"+currentText;
			}
			else
			{
				textArea.setText(textArea.getText().replaceAll("-(\\d+)$","$1"));
				currentText = currentText.substring(1);
			}
			
		}
		if(command.equals("=")&&!currentText.isEmpty())
		{
			evaluate();
		}
		if(command.equalsIgnoreCase("M"))
		{
			String input = JOptionPane.showInputDialog("Select Moduli",Arrays.toString(moduli));
			String[] strs = input.replaceAll("[\\[\\], ]"," ").replaceAll("\\s+"," ").trim().split(" ");
			long[] longs = new long[strs.length];
			for(int i = 0;i<longs.length;i++)
				longs[i] = Long.parseLong(strs[i].trim());
			setModuli(longs);
		}
		this.requestFocus();
	}
	
	public void evaluate()
	{
		System.out.println("Evaluating "+stack+" "+currentText);
		if(stack.isEmpty()){
			long current = Long.parseLong(currentText);
			String toAppend = ""+toStandard(toRNS(current));
			textArea.append("\n\n"+toAppend);
			return;
		}
		char op = stack.pop().charAt(0);
		long a = Long.parseLong(stack.pop());
		long b = Long.parseLong(currentText);
		String ans = "";
		textArea.append("\n");
		switch(op)
		{
		case '-':ans = subtract(a,b)+"";break;
		case '+':ans = add(a,b)+"";break;
		case '*':ans = multiply(a,b)+"";
		}
		currentText = ans;
		textArea.append(currentText);
		if(!stack.isEmpty())
			evaluate();
	}
	
	public long subtract(long a, long b)
	{
		long[] x = toRNS(a);
		long[] y = toRNS(b);
		long[] z = subtract(x,y);
		String toAppend = "  [";
		for(long l:x)
			toAppend+=String.format("%"+maxColumns+"s",l+"")+",";
		toAppend = toAppend.replaceAll(",$", " = "+toStandard(x)+"\n");
		toAppend+="-[";
		for(long l:y)
			toAppend+=String.format("%"+maxColumns+"s",l+"")+",";
		toAppend = toAppend.replaceAll(",$", "] = "+toStandard(y)+"\n  [");
		for(long l:z)
			toAppend+=String.format("%"+maxColumns+"s",l+"")+",";
		toAppend = toAppend.replaceAll(",$", "] = "+toStandard(z)+"\n");
		textArea.append("\n"+toAppend+"\n");
		return toStandard(z);
	}
	
	public long add(long a, long b)
	{
		long[] x = toRNS(a);
		long[] y = toRNS(b);
		long[] z = add(x,y);
		String toAppend = "  [";
		for(long l:x)
			toAppend+=String.format("%"+maxColumns+"s",l+"")+",";
		toAppend = toAppend.replaceAll(",$", "] = "+toStandard(x)+"\n");
		toAppend+="+[";
		for(long l:y)
			toAppend+=String.format("%"+maxColumns+"s",l+"")+",";
		toAppend = toAppend.replaceAll(",$", "] = "+toStandard(y)+"\n  [");
		for(long l:z)
			toAppend+=String.format("%"+maxColumns+"s",l+"")+",";
		System.out.println("here");
		toAppend = toAppend.replaceAll(",$", "] = "+toStandard(z)+"\n");
		System.out.println(toAppend);
		textArea.append("\n"+toAppend+"\n");
		return toStandard(z);
	}
	
	public long multiply(long a, long b)
	{
		long[] x = toRNS(a);
		long[] y = toRNS(b);
		long[] z = multiply(x,y);
		String toAppend = "  [";
		for(long l:x)
			toAppend+=String.format("%"+maxColumns+"s",l+"")+",";
		toAppend = toAppend.replaceAll(",$", "] = "+toStandard(x)+"\n");
		toAppend+="*[";
		for(long l:y)
			toAppend+=String.format("%"+maxColumns+"s",l+"")+",";
		toAppend = toAppend.replaceAll(",$", "] = "+toStandard(y)+"\n  [");
		for(long l:z)
			toAppend+=String.format("%"+maxColumns+"s",l+"")+",";
		toAppend = toAppend.replaceAll(",$", "] = "+toStandard(z)+"\n");
		textArea.append("\n"+toAppend+"\n");
		return toStandard(z);
	}
	
	public void evaluateToPlus()
	{		
		System.out.println("Evaluating to plus "+stack+" "+currentText);
		char op =stack.peek().charAt(0); 
		if(op=='+'||op=='-')
			return;
		stack.pop();
		long a = Long.parseLong(stack.pop());
		long b = Long.parseLong(currentText);
		currentText = a*b+"";
		textArea.append("\n\n"+currentText);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE)
			buttonPressed("backspace");
		if(e.getKeyCode()==KeyEvent.VK_ENTER)
			buttonPressed("=");
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
			buttonPressed(e.getKeyChar()+"");
	}
	
}
