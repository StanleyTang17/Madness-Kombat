package GameState.OptionState;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import GameState.LevelState.GameMode;
import GameState.LevelState.LevelState;

public class GameModeBlock extends OptionBlock {
	
	private int index;
	
	public GameModeBlock(OptionsState optionsState, Rectangle displayBox) {
		super(optionsState, displayBox);
		index = LevelState.getGameMode().ordinal();
	}

	
	public void draw(Graphics2D g) {
		this.drawBox(g);
		g.setFont(new Font("consolas", Font.PLAIN, 24));
		String text;
		text = LevelState.getGameMode().name().replace("_", " ");
		FontMetrics fm = g.getFontMetrics();
		int x = getX() + getWidth() / 2 - fm.stringWidth(text) / 2;
		int y = getY() + getHeight() / 2 + fm.getHeight() / 3;
		g.drawString(text, x, y);

	}

	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER)
		{
			index++;
			if(index == GameMode.values().length) index = 0;
			LevelState.setGameMode(GameMode.values()[index]);
		}
		else
			this.switchBlock(k);

	}

}
