package GameObject.Player.Graphics.ParticleEffect;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

import GameObject.Player.Player;

public class BloodParticle extends ParticleEffect{
	
	private final static int duration = 1000;
	private ArrayList<Blood> bloodSplats;
	
	public BloodParticle(Player player) {
		super(duration, player);
		int bloodCount = 4;
		bloodSplats = new ArrayList<Blood>();
		for(int i = 0; i < bloodCount; i++)
			bloodSplats.add(new Blood(player));
	}

	
	public void update() {
		for(Blood blood : bloodSplats)
			blood.update();
		this.checkFinished();
	}

	
	public void drawParticle(Graphics2D g) {
		for(Blood blood : bloodSplats)
			blood.draw(g);
		
	}
	
	private class Blood
	{
		private double x, y;
		private int radius;
		private double alpha;
		
		public Blood(Player player)
		{
			Random r = new Random();
			int range = 8;
			radius = r.nextInt(20) + 3;
			x = player.getX() + r.nextDouble() * (player.getWidth() / 2 + range - radius) - range;
			y = player.getY() + r.nextDouble() * (player.getHeight() - radius);
			alpha = 254;
		}
		
		public void update()
		{
			alpha -= 2;
		}
		
		public void draw(java.awt.Graphics2D g)
		{
			Color color = new Color((int)alpha, 0, 0, (int)alpha);
			g.setColor(color);
			g.fillOval((int)x, (int)y, radius, radius);
		}
	}
}
