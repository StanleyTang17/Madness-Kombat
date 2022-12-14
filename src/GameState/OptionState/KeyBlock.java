package GameState.OptionState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class KeyBlock extends OptionBlock{
	
	private boolean detecting;
	private String text;
	private int key;
	
	public KeyBlock(OptionsState optionsState, Rectangle box, int key) {
		super(optionsState, box);
		detecting = false;
		this.key = key;
		text = KeyEvent.getKeyText(key);
	}

	
	public void draw(Graphics2D g) {
		this.drawBox(g);
		if(detecting)
			g.setColor(Color.RED);
		else
			g.setColor(Color.BLACK);
		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
		
		g.drawString(text, getX() + 5, getY() + 5 + g.getFontMetrics().getHeight());
	}

	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER)
		{
			detecting = true;
		}
		else if(k == KeyEvent.VK_ESCAPE)
		{
			detecting = false;
		}
		else
		{
			if(detecting)
			{
				key = k;
				text = KeyEvent.getKeyText(k);
				detecting = false;
			}
		}
		switchBlock(k);
	}
	
	public int getKey() { return key; }
	public String getText() { return text; }
	
}
