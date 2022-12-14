package GameState.LevelState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import GameObject.Weapon.Weapon;
import GameState.GameState;

public class PauseState extends GameState{
	
	private LevelState ls;
	private String[] texts = { "Game Paused",
							   "[ENTER] to exit",
							   "[R] to restart" };
	
	public PauseState(LevelState levelState) {
		ls = levelState;
	}

	
	public void init() {
		ls.pauseMusic();
		
	}

	
	public void update() {
		
		
	}

	
	public void draw(Graphics2D g) {
		ls.getState(LevelState.PLAYING).draw(g);
		g.setFont(new Font("consolas", Font.PLAIN, 24));
		g.setColor(Color.BLACK);
		for(int i = 0; i < texts.length; i++)
		{
			g.drawString(texts[i], 80, 140 + i * 36);
		}
	}

	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ESCAPE)
		{
			ls.setState(LevelState.PLAYING);
			ls.resumeMusic();
		}
		else if(k == KeyEvent.VK_ENTER)
		{
			ls.exitLevelState();
			while(!Weapon.WeaponList.isEmpty()) Weapon.WeaponList.get(0).removeThis();
		}
		else if(k == KeyEvent.VK_R)
		{
			ls.initState(LevelState.PLAYING);
			ls.setState(LevelState.PLAYING);
			ls.restartMusic();
		}
		
	}

	
	public void keyReleased(int k) {
		
		
	}

}
