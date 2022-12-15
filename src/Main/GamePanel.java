package Main;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.io.File;

import javax.swing.*;

import GameState.GameStateManager;

/*	6104 LINES OF CODE SO FAR	8045 LINES INCLUDING MULTIPLAYER	8474 LINES INCLUDING USELESS JOYSTICK*/

public class GamePanel extends JPanel implements Runnable, KeyListener
{
	private static final long serialVersionUID = 8065096463684789131L;
	
	public static final int WIDTH = 1366;
	public static final int HEIGHT = 768;
	public static final Dimension dimension = new Dimension(WIDTH, HEIGHT);
	public static final String NAME = "Madness Kombat";
	
	private Thread thread;
	private boolean running = false;
	
	private int FPS = 60;
	private long targetTime = 1000 / FPS;
	
	private GameStateManager gsm;
	private JFrame motherFrame;
	private static String levelName = "1";
	private static File level;
	private static File configs;
	
	public GamePanel(JFrame motherFrame)
	{
		super();
		setPreferredSize(dimension);
		setFocusable(true);
		requestFocus();
		this.motherFrame = motherFrame;
	}
	
	public void addNotify()
	{
		super.addNotify();
		if(thread == null)
		{
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}
	
	private void init()
	{
		running = true;
		gsm = new GameStateManager(this);
	}
	
	@SuppressWarnings("static-access")
	public void run() {
		init();
		long start, elapsed, wait, last, second = 1000000000;
		int ticks = 0;
		last = System.nanoTime();
		try {
			//wait for keyListener to load
			thread.sleep(20);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(running)
		{
			start = System.nanoTime();
			
			update();
			draw();
			//drawToScreen();
			ticks++;
			elapsed = System.nanoTime() - start;
			
			if(System.nanoTime() - last > second)
			{
				FPS = ticks;
				last = System.nanoTime();
				ticks = 0;
			}
			
			wait = targetTime - elapsed / 1000000;
			if(wait < 0) wait = 5;
			try
			{
				Thread.sleep(wait);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private void update()
	{
		gsm.update();
	}
	
	private void draw()
	{
		BufferStrategy bs = motherFrame.getBufferStrategy();
		if(bs == null)
		{
			motherFrame.createBufferStrategy(2);
			return;
		}
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		gsm.draw(g);
		g.setColor(Color.BLACK);
		g.setFont(new Font("consolas", Font.PLAIN, 12));
		g.drawString("FPS: " + String.valueOf(FPS), 0, 13);
		g.dispose();
		bs.show();
	}
	
	public void keyTyped(KeyEvent key)
	{
		
	}
	
	public void keyPressed(KeyEvent key)
	{
		gsm.keyPressed(key.getKeyCode());
	}
	
	public void keyReleased(KeyEvent key)
	{
		gsm.keyReleased(key.getKeyCode());
	}
	
	public static String getLevelName()
	{
		return levelName;
	}
	
	public static void setLevelName(String s)
	{
		levelName = s;
	}
	
	public static String getRootFolderPath()
	{
		return System.getProperty("user.dir") + File.separator;
	}
	
	public static String getConfigFilePath()
	{
		return getRootFolderPath() + "configs.txt";
	}
	
	public static String getLevelFilePath()
	{
		return getRootFolderPath() + levelName;
	}
	
	public static File getLevelFile()
	{
		if(level == null)
			level = new File(getLevelFilePath());
		return level;
	}
	
	public static void setLevelFile(File levelFile)
	{
		level = levelFile;
	}
	
	public static File getConfigsFile()
	{
		return configs;
	}
	
	public static void setConfigsFile(File configs)
	{
		GamePanel.configs = configs;
	}
	
	public static void main(String[] args)
	{
		
		JFrame j = new JFrame("Madness Kombat");
		GamePanel g = new GamePanel(j);
		j.setPreferredSize(dimension);
		j.add(g);
		j.pack();
		j.setResizable(false);
		j.setLocationRelativeTo(null);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		j.setVisible(true);
		
	}
}