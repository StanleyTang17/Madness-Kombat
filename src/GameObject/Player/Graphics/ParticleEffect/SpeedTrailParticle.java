package GameObject.Player.Graphics.ParticleEffect;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import GameObject.Player.Player;

public class SpeedTrailParticle extends ParticleEffect {
	
	private ArrayList<Shadow> shadows;
	private long spawnTime;
	private int milliPerShadow;
	private BufferedImage img;
	private int shadowDuration;
	
	public SpeedTrailParticle(Player player, int duration) {
		super(duration, player);
		shadows = new ArrayList<Shadow>();
		milliPerShadow = 40;
		spawnTime = System.currentTimeMillis() + milliPerShadow;
		img = player.getImage();
		shadowDuration = 300;
	}

	
	public void update() {
		long cur = System.currentTimeMillis();
		if(cur > spawnTime)
		{
			spawnTime = cur + milliPerShadow;
			shadows.add(new Shadow(player.getX(), player.getY()));
		}
		for(Shadow s : shadows)
		{
			s.update();
			if(s.finished)
			{
				shadows.remove(s);
				break;
			}
		}
		this.checkFinished();
	}

	
	public void drawParticle(Graphics2D g) {
		for(Shadow s : shadows) s.draw(g);
		
	}
	
	private class Shadow
	{
		private double x, y;
		private float opacity;
		private long endTime;
		private boolean finished;
		
		public Shadow(double x, double y)
		{
			this.x = x;
			this.y = y;
			opacity = 0.7f;
			endTime = System.currentTimeMillis() + shadowDuration;
		}
		
		public void update()
		{
			opacity -= 0.02f;
			if(System.currentTimeMillis() > endTime)
				finished = true;
		}
		
		public void draw(Graphics2D g)
		{
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
			g.drawImage(img, (int)x, (int)y, null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		}
		
		
	}
}
