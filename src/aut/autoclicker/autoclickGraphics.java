package aut.autoclicker;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.dispatcher.SwingDispatchService;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.jnativehook.mouse.NativeMouseListener;
import org.jnativehook.mouse.NativeMouseMotionListener;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import org.jnativehook.mouse.NativeMouseWheelListener;

public class autoclickGraphics extends JPanel implements MouseListener, NativeKeyListener, NativeMouseListener, ChangeListener
{
	private static final long serialVersionUID = 1L;

	private class SwingExecutorService extends AbstractExecutorService 
	  {
	        private EventQueue queue;

	        public SwingExecutorService() {
	            queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
	        }

	        public void shutdown() {
	            queue = null;
	        }

	        public List<Runnable> shutdownNow() {
	            return new ArrayList<Runnable>(0);
	        }

	        public boolean isShutdown() {
	            return queue == null;
	        }

	        public boolean isTerminated() {
	            return queue == null;
	        }

	        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
	            return true;
	        }


			public void execute(Runnable r) {
	            EventQueue.invokeLater(r);
	        }
	    }
	
	private JFrame frame;
	
	private JSlider clickSlider, moveSlider;
	
	private boolean start = true;
	private boolean startdone = false;
	
	private autoclick ac;
	
	private boolean clicking = false;
	private boolean moving = false;
	
	private boolean backwards1 = false;
	private boolean backwards2 = false;
	
	private boolean firstTime = true;
	
	private boolean moveOn = true;
	private boolean clickOn = true;
	
	private int count = 0;
	
	private int moveY = 420;
	private int startMoveY = 400;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public autoclickGraphics()
	{
		
		GlobalScreen.setEventDispatcher(new SwingExecutorService());

		frame = new JFrame("Autoclicker + Automover");
		ac = new autoclick();
		frame.setBackground(new Color(222,219,246));
		ac.setCSpeed(20);
		ac.setMSpeed(10);
		
		setVisible(true);
		addMouseListener(this);
		try {
			frame.setIconImage(ImageIO.read(getClass().getResource("download.png")));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		frame.add(this);
		frame.setSize(700,750);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);	
		
		
		clickSlider = new JSlider(0,20,20);
		moveSlider = new JSlider(0,10,10);
		
		clickSlider.setPaintTrack(true); 
	    clickSlider.setPaintTicks(true); 
	    clickSlider.setPaintLabels(true);
	    clickSlider.setMajorTickSpacing(5);
	    clickSlider.setMinorTickSpacing(1);
	    
	    moveSlider.setPaintTrack(true); 
	    moveSlider.setPaintTicks(true); 
	    moveSlider.setPaintLabels(true);
	    moveSlider.setMajorTickSpacing(5);
	    moveSlider.setMinorTickSpacing(1);
	    
	    Hashtable positionC = new Hashtable();
	    positionC.put(0, new JLabel("0"));
		positionC.put(5, new JLabel("5"));
		positionC.put(10, new JLabel("10"));
		positionC.put(15, new JLabel("15"));
		positionC.put(20, new JLabel("20"));
		
		Hashtable positionM = new Hashtable();
		positionM.put(0, new JLabel("0"));
		positionM.put(5, new JLabel("5"));
		positionM.put(10, new JLabel("10"));
		
		clickSlider.setLabelTable(positionC);
		moveSlider.setLabelTable(positionM);
		
		clickSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(start && clickOn)
				{
					ac.setCSpeed(((JSlider)e.getSource()).getValue());
					repaint();
				}
			}
		});
		
		moveSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(start && moveOn)
				{
					ac.setMSpeed(((JSlider)e.getSource()).getValue());
					repaint();
				}
			}
		});
		
		
		 try {
			 GlobalScreen.registerNativeHook();
	     }
		 catch (NativeHookException ex) {
			 ex.printStackTrace();
		 }
		 
		GlobalScreen.addNativeKeyListener(this);
		GlobalScreen.addNativeMouseListener(this);
        
	}
	
	public void paintComponent(Graphics g)
	{
		//input values
		if(start)
		{
			
			g.setColor(new Color(222,219,246));
			g.fillRect(0, 0, 1920, 1080);
			
			clickSlider.setOpaque(false);
			moveSlider.setOpaque(false);
			clickSlider.setForeground(Color.black);
			moveSlider.setForeground(Color.black);
			clickSlider.setBounds(195, 200, 300, 100);
			moveSlider.setBounds(195, startMoveY, 300, 100);
			if(!moveOn)
				moveSlider.setVisible(false);
			else
				moveSlider.setVisible(true);
			
			if(!clickOn)
			{
				clickSlider.setVisible(false);
				startMoveY = 250;
			}
			else
			{
				clickSlider.setVisible(true);
				startMoveY = 400;
			}
			
			if(count==0)
			{
				this.add(clickSlider);
				
				this.add(moveSlider);
				
				repaint();
				if(firstTime)
				{
					clickSlider.setValue(20);
					moveSlider.setValue(10);
				}
				else
				{
					clickSlider.setValue((int)ac.cSpeed);
					moveSlider.setValue(ac.mSpeed);
				}
			}
			if(clickOn && moveOn)
			{
				if(ac.cSpeed>0 && ac.mSpeed>0)
					startdone = true;
				else
					startdone = false;
			}
			else if(clickOn)
			{
				if(ac.cSpeed>0)
					startdone = true;
				else
					startdone = false;
			}
			else if(moveOn)
			{
				if(ac.mSpeed>0)
					startdone = true;
				else
					startdone = false;
			}
			else
			{
				startdone = false;
			}
			
			g.setColor(Color.black);
			g.setFont(new Font("Times New Roman", Font.BOLD, 17));
			g.drawString("ESC to exit", 15, 30);
			
			if(clickOn)
			{
				g.setFont(new Font("Roboto", Font.BOLD | Font.CENTER_BASELINE, 18));
				g.setColor(Color.red);
				g.drawString("DISABLE", 307, 160);
				
				g.setFont(new Font("Trebuchet MS Bold", Font.BOLD, 35));
				g.setColor(Color.black);
				if(ac.cSpeed > 0)
					g.drawString("CPS: "+String.format(java.util.Locale.US,"%.0f", ac.cSpeed), 283, 200);
				else
					g.drawString("CPS: ", 310, 200);
			}
			else
			{
				g.setFont(new Font("Roboto", Font.BOLD | Font.CENTER_BASELINE, 18));
				g.setColor(Color.green);
				g.drawString("ENABLE AUTOCLICKER", 245, 165);
			}
			
			if(moveOn)
			{
				g.setFont(new Font("Roboto", Font.BOLD | Font.CENTER_BASELINE, 18));
				g.setColor(Color.red);
				g.drawString("DISABLE", 307, startMoveY-35);
				
				g.setFont(new Font("Trebuchet MS Bold", Font.BOLD, 35));
				g.setColor(Color.black);
				
				if(ac.mSpeed>0)
					g.drawString("Move speed: "+ac.mSpeed, 225, startMoveY);
				else
					g.drawString("Move speed: ", 245, startMoveY);
			}
			else
			{
				g.setFont(new Font("Roboto", Font.BOLD | Font.CENTER_BASELINE, 18));
				g.setColor(Color.green);
				g.drawString("ENABLE AUTOMOVER", 252, startMoveY-35);
			}
			
			if(startdone)
			{
				g.setColor(Color.white);
				g.fillRect(304, startMoveY+137, 85, 37);
				g.setColor(Color.black);
				g.setFont(new Font("Arial", Font.BOLD, 25));
				g.drawString("DONE", 310, startMoveY+165);
			}
			count++;
		}
		//finished inputting values
		else
		{
			this.remove(moveSlider);
			this.remove(clickSlider);
			
			if(!clickOn)
				moveY = 270;
			else
				moveY = 420;
			
			g.setColor(new Color(222,219,246));
			g.fillRect(0, 0, 1920, 1080);
			
			g.setColor(Color.black);
			g.setFont(new Font("Times New Roman", Font.BOLD, 17));
			g.drawString("ESC to exit", 15, 30);
			
			g.setFont(new Font("Roboto", Font.BOLD, 23));
			g.setColor(Color.black);
			
			
			if(clicking) //autoclicker on
			{
				paintText(g);
				g.setColor(Color.green);
				g.setFont(new Font("Roboto", Font.BOLD, 24));
				g.drawString("Autoclicker on", 150, 270);
				
				g.setFont(new Font("Roboto", Font.BOLD, 24));
				g.setColor(Color.red);
				if(moveOn)
					g.drawString("Automover off", 150, moveY);
					
				//autoclick();
			}
			else if(moving) //automover on
			{
				paintText(g);
				
				g.setColor(Color.red);
				g.setFont(new Font("Roboto", Font.BOLD, 24));
				if(clickOn)
					g.drawString("Autoclicker off", 150, 270);
				
				g.setFont(new Font("Roboto", Font.BOLD, 24));
				g.setColor(Color.green);
				g.drawString("Automover on", 150, moveY);
				
				//automove();
				
			}
			//neither on
			else 
			{
				paintText(g);
				
				g.setColor(Color.red);
				g.setFont(new Font("Roboto", Font.BOLD, 24));
				if(clickOn)
					g.drawString("Autoclicker off", 150, 270);
				
				g.setFont(new Font("Roboto", Font.BOLD, 24));
				g.setColor(Color.red);
				if(moveOn)
					g.drawString("Automover off", 150, moveY);
				
				g.setColor(Color.white);
				g.fillRect(299, 119, 82, 32);
				g.setColor(Color.black);
				g.setFont(new Font("Roboto", Font.BOLD, 21));
				g.drawString("RESET", 305, 143);
			}
		}
	}
	public void paintText(Graphics g)
	{
		g.setColor(Color.black);
		g.setFont(new Font("Roboto", Font.BOLD, 14));
		if(clickOn)
			g.drawString("press middle mouse button to toggle", 360, 270);
		if(moveOn)
			g.drawString("press LALT to toggle", 360, moveY);
	}
	//automover
	public void automove() 
	{
		Runnable runnable = new Runnable()
		{
			@Override
			public void run()
			{
		
			try 
			{ 
				Robot r = new Robot(); 
				while(moving)
				{
					int a = (int)Math.random();
					Point p = MouseInfo.getPointerInfo().getLocation();
					int two = 1600 + ((int)(Math.random()*400-200));
					int one = 300+ ((int)(Math.random()*400-200));
					if(p.x<one)
					{
						r.mouseMove(p.x+(int)(ac.mSpeed), p.y);
						backwards1 = true;
						backwards2 = false;
					}
					else if(p.x>two)
					{
						r.mouseMove(p.x-(int)(ac.mSpeed), p.y);
						backwards2 = true;
						backwards1 = false;
					}
					else if(backwards1)
					{
						r.mouseMove(p.x+(int)(ac.mSpeed), p.y);
						Thread.sleep(15/(ac.mSpeed));
					}
					else if(backwards2)
					{
						r.mouseMove(p.x-(int)(ac.mSpeed), p.y);
						Thread.sleep(15/(ac.mSpeed));
					}
					else 
					{
						
						if(a==0)
						{
							r.mouseMove(p.x+(int)(ac.mSpeed), p.y);
							Thread.sleep(15/(ac.mSpeed));
						}
						else if(a==1)
						{
							r.mouseMove(p.x-(int)(ac.mSpeed), p.y);
							Thread.sleep(15/(ac.mSpeed));
						}
					}
				}
		 
			} 
			catch (Exception e) { 
				e.printStackTrace();
			}
			
			}
	
		};
		
		Thread movingThread = new Thread(runnable);
		movingThread.start();
	}
	//autoclicker
	public void autoclick()
	{
		Runnable runnable = new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Robot r = new Robot();
					while(clicking)
					{
						double delay = Math.random()*13+(ac.delay-9);
						r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
						r.delay((int)Math.random()*7);
						r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
						r.delay((int)delay);
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		};
		Thread clickingThread = new Thread(runnable);
		clickingThread.start();

	}
	@Override
	public void mouseClicked(MouseEvent e) {
		//"reset" button
		if(e.getX() >= 298 && e.getX() <= 380 && e.getY() >= 120 && e.getY() <= 150 && !start && !clicking && !moving)
		{
			start = true;
			startdone = false;
			clicking = false;
			if(firstTime)
				firstTime = !firstTime;
			count = 0;
			clickSlider.setValue((int)(ac.cSpeed));
			moveSlider.setValue(ac.mSpeed);
			
			repaint();
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		System.out.println("X: "+e.getX()+ ", Y: "+e.getY());
		
		//"done" button
		if(e.getX() >= 302 && e.getX() <= 390 && e.getY() >= 535 && e.getY() <= 575 && start && startdone && clickOn) 
		{
			start = false;
			repaint();
		}
		//higher y-coor "done" button
		else if(e.getX() >= 302 && e.getX() <= 390 && e.getY() >= 385 && e.getY() <= 425 && start && startdone && !clickOn) 
		{
			start = false;
			repaint();
		}
		
		//"disable autoclicker" button
		if(e.getX() >= 307 && e.getX() <= 382 && e.getY() >= 140 && e.getY() <= 165 && start &&clickOn)
		{
			clickOn = !clickOn;
			repaint();
		}
		//enable
		else if(e.getX() >= 245 && e.getX() <= 450 && e.getY() >= 140 && e.getY() <= 165 && start &&!clickOn)
		{
			clickOn = !clickOn;
			repaint();
		}
		
		//"disable automover" button
		else if(e.getX() >= 307 && e.getX() <= 382 && e.getY() >= 345 && e.getY() <= 370 && start && moveOn && clickOn)
		{
			moveOn = !moveOn;
			repaint();
		}
		//enable
		else if(e.getX() >= 245 && e.getX() <= 450 && e.getY() >= 345 && e.getY() <= 370 && start && !moveOn && clickOn)
		{
			moveOn = !moveOn;
			repaint();
		}
		//higher position buttons (enable/disable)
		//"disable automover" button
		else if(e.getX() >= 307 && e.getX() <= 382 && e.getY() >= 195 && e.getY() <= 220 && start && moveOn && !clickOn)
		{
			moveOn = !moveOn;
			repaint();
		}
		//enable
		else if(e.getX() >= 245 && e.getX() <= 450 && e.getY() >= 195 && e.getY() <= 220 && start && !moveOn && !clickOn)
		{
			moveOn = !moveOn;
			repaint();
		}
		
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) 
	{
		if(e.getKeyCode()== NativeKeyEvent.VC_ALT && !start && moveOn) //toggle automover
		{
			clicking = false;
			if(!moving && !start)
			{
				moving = true;
				repaint();
				automove();
			}
			else if(moving)
			{
				moving = false;
				backwards1 = false;
				backwards2 = false;
				repaint();
			}
		}
		else if(e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) //close program
		{
			try {
				GlobalScreen.unregisterNativeHook();
			} catch (NativeHookException e1) {
				e1.printStackTrace();
			}
			System.exit(1);
			frame.setVisible(false);
			frame.dispose();
		}
	}
	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {

	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nativeMouseClicked(NativeMouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nativeMousePressed(NativeMouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent e) 
	{
		if(e.getButton() == NativeMouseEvent.BUTTON3 && !start && clickOn) //toggle autoclicker
		{
			moving = false;
			if(!clicking && !start)	
			{
				clicking = true;
				repaint();
				autoclick();
			}
			else if(clicking)
			{
				clicking = false;
				repaint();
			}
		}
		
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
