package GameState.OptionState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

public class TextBlock extends OptionBlock{
	
	private String text;
	private boolean editing;
	
	public TextBlock(OptionsState optionsState, Rectangle box, String text) {
		super(optionsState, box);
		this.text = text;
	}

	
	public void draw(Graphics2D g) {
		this.drawBox(g);
		g.setFont(new Font("consolas", Font.PLAIN, 24));
		if(editing)
			g.setColor(Color.RED);
		else
			g.setColor(Color.BLACK);
		g.drawString(text, getX() + 10, getY() + g.getFontMetrics().getHeight() + 5);
	}

	
	public void keyPressed(int k) {
		
		if(k == KeyEvent.VK_ENTER)
		{
			String retrievedText = JOptionPane.showInputDialog("Enter player name");
			if(retrievedText != null) text = retrievedText;
		}
		else if(k == KeyEvent.VK_UP || k == KeyEvent.VK_DOWN || k == KeyEvent.VK_LEFT || k == KeyEvent.VK_RIGHT)
		{
			this.switchBlock(k);
			editing = false;
		}
		else
		{
			if(editing) text += Character.toLowerCase((char)k);
		}
		
	}
	
	public String getText() { return text; }
	public void setText(String text) { this.text = text; }
}
