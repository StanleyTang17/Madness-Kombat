package Multiplayer;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame implements Runnable{

	private static final long serialVersionUID = 1L;
	
	private boolean running = true;
	private WindowListener wl;
	private Thread thread;
	
	public Window(JPanel panel, WindowListener windowsListener, Dimension dimension, String name) {
		
		wl = windowsListener;
		
		this.setPreferredSize(dimension);
		this.setTitle(name);
		this.add(panel);
		this.pack();
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
	}

	public void addNotify()
	{
		super.addNotify();
		if(thread == null)
		{
			thread = new Thread(this);
			thread.start();
		}
	}
	
	public void run() {
		while(running)
		{
			draw();
		}
		
	}
	
	public void draw()
	{
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null)
		{
			this.createBufferStrategy(2);
			return;
		}
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		wl.draw(g);
		g.dispose();
		bs.show();
	}
	
	public void stop()
	{
		running = false;
		this.stop();
		this.dispose();
	}
}
