package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;

import Main.GamePanel;

public class MenuState extends GameState{
	
	private String[] options = {"Start","Editor","Options","Quit"};
	private int currentChoice = 0;
	
	private Color titleColor;
	private Font titleFont;
	private Font font;
	
	public MenuState(GameStateManager gsm)
	{
		this.gsm = gsm;
		titleColor = Color.BLACK;
		titleFont = new Font("Comic Sans MS", Font.PLAIN, 48);
		font = new Font("Consolas", Font.PLAIN, 24);
	}
	
	public void init() {
		
		
	}

	public void update() {
		gsm.bg.update();
	}

	public void draw(Graphics2D g) {
		gsm.bg.draw(g);
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString(GamePanel.NAME, 80, 160);
		g.setFont(font);
		for(int i = 0; i < options.length; i++)
		{
			if(i == currentChoice)
			{
				g.setColor(Color.RED);
			}
			else
			{
				g.setColor(Color.BLACK);
			}
			g.drawString(options[i], 80, 220 + i * 36);
		}
	}

	private void select()
	{
		switch(currentChoice)
		{
		case 0:
			gsm.setState(GameStateManager.LEVELSTATE);
			break;
		case 1:
			gsm.setState(GameStateManager.EDITORSTATE);
			break;
		case 2:
			gsm.setState(GameStateManager.OPTIONSTATE);
			break;
		case 3:
			System.exit(0);
			break;
		}
	}
	
	public void keyPressed(int k) {
		switch(k)
		{
		case KeyEvent.VK_ENTER:
			select();
			break;
		case KeyEvent.VK_UP:
			currentChoice--;
			if(currentChoice == -1)
			{
				currentChoice = options.length - 1;
			}
			break;
		case KeyEvent.VK_DOWN:
			currentChoice++;
			if(currentChoice == options.length)
			{
				currentChoice = 0;
			}
			break;
			
		}
		
		
	}

	public void keyReleased(int k) {
		
	}
	
}
