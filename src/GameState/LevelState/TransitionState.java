package GameState.LevelState;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import GameObject.Player.Player;
import GameObject.Player.Team;
import GameState.GameState;
import Main.GamePanel;

public class TransitionState extends GameState{
	
	private LevelState ls;
	private Player winner;
	private long endTime;
	private long duration;
	private double margin;
	
	public TransitionState(LevelState levelState) {
		ls = levelState;
		duration = 3000;
		
	}

	
	public void init() {
		margin = 300;
		endTime = System.currentTimeMillis() + duration;
		
	}

	
	public void update() {
		if(endTime - System.currentTimeMillis() > 2000)
			margin -= 2.5;
		if(System.currentTimeMillis() >= endTime)
		{
			ls.initState(LevelState.PLAYING);
			ls.setState(LevelState.PLAYING);
		}
	}

	
	public void draw(Graphics2D g) {
		PlayingState ps = (PlayingState) ls.getState(LevelState.PLAYING);
		
		ps.draw(g);
		GameMode gm = LevelState.getGameMode();
		if(gm == GameMode.LAST_MAN_STANDING)
		{
			winner = ps.getWinningPlayer();
			if(winner != null)
			{
				g.setStroke(new BasicStroke(5));
				int px = (int)winner.getX() + winner.getWidth() / 2;
				int py = (int)winner.getY() + winner.getHeight() / 2;
				g.setColor(Color.GREEN);
				int m = (int) margin;
				g.drawOval(px - m / 2, py - m / 2, m, m);
				g.setStroke(new BasicStroke(1));
				
				drawResult(g, winner.getName() + " wins!");
			}
			else
			{
				drawResult(g, "No one wins...");
				System.out.println("dsfdsfdsfsd");
			}
		}
		else if(gm == GameMode.TEAM)
		{
			Team winningTeam = ps.getWinningPlayer().getTeam();
			drawResult(g, winningTeam.name() + " team wins!");
		}
		
		
	}
	
	private void drawResult(Graphics2D g, String result)
	{
		g.setFont(new Font("impact", Font.PLAIN, 60));
		g.setColor(Color.BLACK);
		g.drawString(result, 
				GamePanel.WIDTH / 2 - g.getFontMetrics().stringWidth(result) / 2, 
				GamePanel.HEIGHT / 2 + g.getFontMetrics().getHeight() / 2);
	}
	
	public void keyPressed(int k) {
		
		
	}

	
	public void keyReleased(int k) {
		
		
	}

}
