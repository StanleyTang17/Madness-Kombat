package GameState.OptionState;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public abstract class OptionBlock {
	
	private OptionBlock[] nearbyBlocks = new OptionBlock[4];
	private boolean[] blockExists = new boolean[4];
	private OptionsState optionsState;
	private Rectangle displayBox;
	private boolean selected;
	private int x, y, width, height;
	
	public static final int UPPER_BLOCK = 0;
	public static final int BOTTOM_BLOCK = 1;
	public static final int LEFT_BLOCK = 2;
	public static final int RIGHT_BLOCK = 3;
	
	public OptionBlock(OptionsState optionsState, Rectangle displayBox) {
		this.optionsState = optionsState;
		for(int i = 0; i < blockExists.length; i++) blockExists[i] = false;
		this.displayBox = displayBox;
		x = displayBox.x;
		y = displayBox.y;
		width = displayBox.width;
		height = displayBox.height;
	}
	
	public abstract void draw(Graphics2D g);
	public abstract void keyPressed(int k);
	
	public void switchBlock(int k)
	{
		if(k == KeyEvent.VK_UP)
		{
			if(!blockExists[UPPER_BLOCK]) return;
			optionsState.setOption(this.getNearbyBlock(UPPER_BLOCK));
			this.selected = false;
		}
		else if(k == KeyEvent.VK_DOWN)
		{
			if(!blockExists[BOTTOM_BLOCK]) return;
			optionsState.setOption(this.getNearbyBlock(BOTTOM_BLOCK));
			this.selected = false;
		}
		else if(k == KeyEvent.VK_LEFT)
		{
			if(!blockExists[LEFT_BLOCK]) return;
			optionsState.setOption(this.getNearbyBlock(LEFT_BLOCK));
			this.selected = false;
		}
		else if(k == KeyEvent.VK_RIGHT)
		{
			if(!blockExists[RIGHT_BLOCK]) return;
			optionsState.setOption(this.getNearbyBlock(RIGHT_BLOCK));
			this.selected = false;
		}
	}
	
	public void drawBox(Graphics2D g)
	{
		if(selected)
			g.setColor(Color.DARK_GRAY);
		else
			g.setColor(Color.GRAY);
		g.fill(displayBox);
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(3));
		g.draw(displayBox);
		g.setStroke(new BasicStroke(1));
	}
	
	public int getWidth() { return width; }
	public int getHeight () { return height; }
	public int getX() { return x; }
	public void setX(int x) { this.x = x; }
	public int getY() { return y; }
	public void setY(int y) { this.y = y; }
	public void setSelected(boolean b) { selected = b; }
	public void setNearbyBlock(int index, OptionBlock block) { blockExists[index] = true; nearbyBlocks[index] = block; }
	public OptionBlock getNearbyBlock(int index) { return nearbyBlocks[index]; }
	public OptionsState getOptionsState() { return optionsState; }
}
