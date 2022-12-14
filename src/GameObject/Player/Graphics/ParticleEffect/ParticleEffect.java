package GameObject.Player.Graphics.ParticleEffect;

import GameObject.Player.Player;

public abstract class ParticleEffect {

	public boolean finished = false;
	public long startTime;
	public int duration;
	public Player player;
	public void checkFinished() { 
		if(System.currentTimeMillis() > startTime + duration) {
			finished = true; 
		} 
	}
	public ParticleEffect(int duration, Player player) { 
		startTime = System.currentTimeMillis(); 
		this.duration = duration; 
		this.player = player; 
		finished = false; 
	}
	public boolean isFinished() { return finished; }
	public abstract void update();
	public abstract void drawParticle(java.awt.Graphics2D g);
	
}
