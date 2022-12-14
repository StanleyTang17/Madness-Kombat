package GameState.OptionState;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import GameObject.Player.Team;
import GameState.OptionState.OptionBlock;

public class TeamOptionBlock extends OptionBlock {
	
	private Team team;
	private int index;
	
	public TeamOptionBlock(OptionsState optionsState, Rectangle displayBox, Team team) {
		super(optionsState, displayBox);
		this.team = team;
		index = team.ordinal();
	}

	
	public void draw(Graphics2D g) {
		this.drawBox(g);
		g.setColor(team.getColor(team));
		g.setFont(new Font("consolas", Font.PLAIN, 24));
		String text;
		text = team.name();
		FontMetrics fm = g.getFontMetrics();
		int x = getX() + getWidth() / 2 - fm.stringWidth(text) / 2;
		int y = getY() + getHeight() / 2 + fm.getHeight() / 3;
		g.drawString(text, x, y);

	}

	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER)
		{
			index++;
			if(index == Team.values().length) index = 0;
			team = Team.values()[index];
		}
		else
			this.switchBlock(k);

	}
	
	public Team getTeam() { return team; }
	public void setTeam(Team team) { this.team = team; }

}
