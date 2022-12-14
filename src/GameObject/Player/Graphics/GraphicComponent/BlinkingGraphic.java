package GameObject.Player.Graphics.GraphicComponent;

import java.awt.Graphics2D;

import GameObject.Player.Player;
import GameObject.Player.Graphics.PlayerGraphics;

public class BlinkingGraphic extends GraphicComponent {
	
	private long blinkTime;
	private long blinkFrequency;
	private StaticGraphic Static;
	private long startTime;
	
	public BlinkingGraphic(Player player) {
		super(player);
		blinkFrequency = 70;
		blinkTime = System.currentTimeMillis();
		Static = new StaticGraphic(player);
		startTime = System.currentTimeMillis();
	}

	
	public void update() {
		if(System.currentTimeMillis() - startTime > 1000)
			this.getPlayer().getGraphics().setState(PlayerGraphics.STATIC);
		
	}

	
	public void draw(Graphics2D g) {
		long curTime = System.currentTimeMillis();
		if(curTime <= blinkTime)
		{
			Static.draw(g);
		}
		if(curTime >= blinkTime + blinkFrequency)
		{
			blinkTime = curTime + blinkFrequency;
		}
	}

}
