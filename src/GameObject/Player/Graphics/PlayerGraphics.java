package GameObject.Player.Graphics;

import java.awt.Graphics2D;
import java.util.ArrayList;

import GameObject.Player.Graphics.GraphicComponent.*;
import GameObject.Player.Graphics.ParticleEffect.*;
import GameObject.Player.Player;

public class PlayerGraphics {
	
	private Player player;
	private GraphicComponent graphic;
	private ArrayList<ParticleEffect> particles;
	
	public static final int STATIC = 0;
	public static final int BLINKING = 1;
	public static final int SPEED = 2;
	public static final int HEALING = 3;
	
	public PlayerGraphics(Player player) {
		this.player = player;
		graphic = new BlinkingGraphic(player);
		particles = new ArrayList<ParticleEffect>();
	}
	
	public void update()
	{
		graphic.update();
		updateParticles();
	}
	
	public void draw(Graphics2D g)
	{
		drawParticles(g);
		graphic.draw(g);
	}
	
	public void setState(int state)
	{ 
		switch(state)
		{
		case STATIC:
			graphic = new StaticGraphic(player);
			break;
		case BLINKING:
			graphic = new BlinkingGraphic(player);
			break;
		}
	}
	
	public void updateParticles()
	{
		for(int i = 0; i < particles.size(); i++)
		{
			ParticleEffect cur = particles.get(i);
			cur.update();
			if(cur.isFinished())
			{
				particles.remove(i);
				break;
			}
		}
	}
	
	public void drawParticles(java.awt.Graphics2D g)
	{
		for(int i = 0; i < particles.size(); i++)
			particles.get(i).drawParticle(g);
	}
	
	public void addParticleEffect(ParticleEffect particleEffect) { particles.add(particleEffect); }
	
}
