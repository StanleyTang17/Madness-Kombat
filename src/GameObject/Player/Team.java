package GameObject.Player;

import java.awt.Color;

public enum Team {
	RED,
	BLUE,
	PURPLE,
	YELLOW;
	
	public Color getColor(Team team)
	{
		Color color = Color.BLACK;
		if(team == Team.RED)
		{
			color = Color.RED;
		}
		else if(team == Team.BLUE)
		{
			color = Color.BLUE;
		}
		else if(team == Team.PURPLE)
		{
			color = new Color(148,0,211);
		}
		else if(team == Team.YELLOW)
		{
			color = Color.ORANGE;
		}
		return color;
	}
}

