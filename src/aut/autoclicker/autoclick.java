package aut.autoclicker;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
public class autoclick 
{
	Robot r;
	double delay;
	double cSpeed = -1;
	int mSpeed = -1;
	 
	public autoclick()
	{
		try 
		{
			r = new Robot();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		delay = 1000;
	}
	public void setCSpeed(double n)
	{
		cSpeed = n;
		setDelay(1000/cSpeed);
	}
	public void setMSpeed(int x)
	{
		mSpeed = x;
	}
	public void setDelay(double ms)
	{
		delay = ms;
	}
}

