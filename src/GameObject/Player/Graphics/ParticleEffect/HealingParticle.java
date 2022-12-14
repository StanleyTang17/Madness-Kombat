package GameObject.Player.Graphics.ParticleEffect;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

import GameObject.Player.Player;

public class HealingParticle extends ParticleEffect{
	
	private ArrayList<Cross> crosses;
	private ArrayList<Integer> spawnTimes;
	
	private static final int totalDuration = 800;
	private static final int spawnDuration = 150;
	
	public HealingParticle(Player player) {
		super(totalDuration, player);
		crosses = new ArrayList<Cross>();
		Random r = new Random();
		int count = 5;
		spawnTimes = new ArrayList<Integer>();
		for(int i = 0; i < count; i++)
		{
			int spawnTime = r.nextInt(101);
			spawnTimes.add(spawnTime);
		}
	}

	
	public void update() {
		for(Integer i : spawnTimes)
		{
			if(System.currentTimeMillis() - this.startTime > i)
			{
				crosses.add(new Cross(player));
				spawnTimes.remove(i);
				break;
			}
		}
		for(Cross cross : crosses)
		{
			cross.update();
			if(cross.isFinished())
			{
				crosses.remove(cross);
				break;
			}
		}
		this.checkFinished();
	}
	
	
	public void drawParticle(Graphics2D g) {
		for(Cross cross : crosses)
			cross.draw(g);
	}
	
	private class Cross
	{
		private long startTime;
		private double x, y;
		private boolean finished;
		private int duration;
		
		private static final int length = 5;
		private static final double speed = -0.6;
		
		public Cross(Player player)
		{
			duration = totalDuration - spawnDuration;
			double range = 5;
			Random r = new Random();
			x = r.nextDouble() * (range * 2 + player.getWidth()) + player.getX() - range;
			y = player.getY() + r.nextDouble() * (player.getHeight() - range);
			startTime = System.currentTimeMillis();
		}
		
		public void update()
		{
			long curTime = System.currentTimeMillis();
			y += speed;
			if(curTime > startTime + duration) finished = true;
		}
		
		public void draw(Graphics2D g)
		{
			g.setColor(Color.GREEN);
			g.setStroke(new BasicStroke(5));
			g.drawLine((int)x + length, (int)y, (int)x + length, (int)y + length * 2);
			g.drawLine((int)x, (int)y + length, (int)x + length * 2, (int)y + length);
			g.setStroke(new BasicStroke(1));
		}
		
		public boolean isFinished() { return finished; }
	}

	

}
