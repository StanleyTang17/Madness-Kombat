package GameObject.Weapon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import GameObject.GameObject;
import GameObject.Player.Player;
import Main.SoundEffect;

public class Explosion extends GameObject{
	
	private int blastRadius;
	private int graphicRadius;
	private int damage;
	
	public static ArrayList<Explosion> ExplosionList = new ArrayList<Explosion>();
	public static void updateExplosions() { 
		try
		{
			for(Explosion e : ExplosionList) e.update();
		} catch(Exception e) {}
	}
	public static void drawExplosions(Graphics2D g) { for(Explosion e : ExplosionList) e.draw(g); }
	
	public Explosion(double x, double y, int damage, int blastRadius) {
		this.setX(x);
		this.setY(y);
		this.blastRadius = blastRadius;
		this.damage = damage;
		graphicRadius = 5;
		this.setHitbox(new Rectangle((int)this.getX() - blastRadius / 2, (int)this.getY() - blastRadius / 2, blastRadius, blastRadius));
		try {
			for(Player p : Player.PlayerList)
			{
				if(this.collidesWith(p))
				{
					p.takeDamage(calculateDamage(p.getX() + p.getWidth() / 2, p.getY() + p.getHeight() / 2));
				}
			} 
		} catch(Exception e) {}
		SoundEffect sound = new SoundEffect(getClass().getResource("/explosion.wav"));
		sound.play();
		ExplosionList.add(this);
	}
	
	public void draw(Graphics2D g)
	{
		int i = 255 - (int)((double)graphicRadius / blastRadius * 255);
		g.setColor(new Color(i, i, i, i));
		g.fillOval((int)this.getX() - graphicRadius / 2, (int)this.getY() - graphicRadius / 2, graphicRadius, graphicRadius);
	}
	
	private double calculateDistance(double x1, double y1, double x2, double y2)
	{
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}
	
	private int calculateDamage(double px, double py)
	{
		double dis = calculateDistance(this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() / 2, px, py);
		return (int)((1 - dis / (double)blastRadius) * damage);
	}
	
	public void removeThis() { ExplosionList.remove(this); }
	
	public void init() {
		
		
	}

	
	public void update() {
		int increase = 0;
		if(graphicRadius < blastRadius * 3 / 4)
		{
			increase = 25;
		}else if(graphicRadius > blastRadius)
		{
			this.removeThis();
		}
		else
		{
			increase = 1;
		}
		graphicRadius += increase;
	}
}
