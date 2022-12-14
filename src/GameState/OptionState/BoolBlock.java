package GameState.OptionState;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class BoolBlock extends OptionBlock {
	
	private int bool;
	
	public BoolBlock(OptionsState optionsState, Rectangle displayBox, int bool) {
		super(optionsState, displayBox);
		this.bool = bool;
	}

	
	public void draw(Graphics2D g) {
		this.drawBox(g);
		g.setFont(new Font("consolas", Font.PLAIN, 24));
		String text;
		if(bool == 1) text = "On"; else text = "Off";
		FontMetrics fm = g.getFontMetrics();
		int x = getX() + getWidth() / 2 - fm.stringWidth(text) / 2;
		int y = getY() + getHeight() / 2 + fm.getHeight() / 3;
		g.drawString(text, x, y);
	}

	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER)
		{
			bool *= -1;
		}
		else
		{
			this.switchBlock(k);
		}
	}
	
	public int getBool() { return bool; }
	public void setBool(int bool) { this.bool = bool; }

}
