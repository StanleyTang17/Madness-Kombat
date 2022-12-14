package GameObject.Weapon;

import java.awt.Graphics2D;
import java.awt.Point;

import GameObject.Player.Player;
import TileMap.TileMap;

public class Grenade extends Weapon {
	
	private int damage;
	private long explodeTime;
	private int blastRadius = 400;
	private int timeTillExplosion;
	private int state;
	private double launchXVel, launchYVel;
	
	private static final int HELD = 0;
	private static final int PULLED = 1;
	private static final int LAUNCHED = 2;
	
	public Grenade(TileMap tileMap) {
		super(-1, 0, 0, 0, Weapon.FULLY_AUTOMATIC, -1, "/grenade.png", "/grenade_explosion.wav", tileMap);
		damage = 60;
		state = HELD;
		launchXVel = 3;
		launchYVel = -2;
		timeTillExplosion = 1600;
	}
	
	public void moveWithPlayer(Player p) {
		if(this.getDirection() == FACING_RIGHT)
			this.setX(p.getX() + p.getWidth());
		else
			this.setX(p.getX() - this.getWidth());
		this.setY(p.getY() + 13);
	}
	
	@Override
	public void pullTrigger()
	{
		state = PULLED;
		if(launchXVel < 16)
			launchXVel += 0.1;
		if(launchYVel > -16)
			launchYVel += -0.2;
	}
	
	@Override
	public void releaseTrigger()
	{
		if(!this.isEquipped() || state != PULLED) return;
		state = LAUNCHED;
		explodeTime = System.currentTimeMillis() + timeTillExplosion;
		this.setXVel(launchXVel * this.getDirection());
		this.setYVel(launchYVel);
		this.getPlayer().dropWeapon();
		this.setInAir(true);
	}
	
	public void shootBullet() {
		

	}

	
	public void init() {
		

	}

	@Override
	public void update()
	{
		if(this.isEquipped()) 
		{ 
			moveWithPlayer(this.getPlayer());
		} 
		else 
		{
			if(this.getY() > 700)
			{
				if(state == LAUNCHED)
					this.removeThis();
				else
					respawn();
				return;
			}
			terrainCollision();
			move();
			if(getYVel() == 0) this.setEquipable(true);;
		}
		updateHitbox();
		checkTriggerPulled();
	}
	
	public void draw(Graphics2D g) {
		this.drawImage(g);
		
		if(state == LAUNCHED)
		{
			if(!this.inAir())
			{
				if(this.getXVel() != 0)
				{
					int dir = this.getDirection();
					this.setXVel(this.getXVel() - dir * 0.7);
					if(dir == FACING_LEFT && this.getXVel() > 0) this.setXVel(0);
					else if(dir == FACING_RIGHT && this.getXVel() < 0) this.setXVel(0);
				}
			}
			if(System.currentTimeMillis() > explodeTime)
			{
				new Explosion(this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() / 2, damage, blastRadius);
				this.removeThis();
			}
		}
	}
	
	
	
	
	/*
	private double[] calculateKnockback(double px, double py)
	{
		double[] force = new double[2];
		double x = px - this.getX();
		double y = py - this.getY();
		double radius = 125;
		double overallForce = (1 - Math.sqrt(px * px + py * py) / radius) * knockback;
		force[0] = overallForce * Math.cos(x);
		force[1] = overallForce * Math.sin(y);
		if(x < 0) force[0] *= -1;
		if(y < 0) force[1] *= -1;
		System.out.println("forceX: " + String.valueOf(force[0]) + " forceY: " + String.valueOf(force[1]));
		return force;
	}
	*/
	@Override
	public void spawn(Point spawnPoint)
	{
		Point p = spawnPoint;
		setX(p.y * TileMap.getTileSize());
		setY(p.x * TileMap.getTileSize());
		state = HELD;
		launchXVel = 3;
		launchYVel = -2;
	}
}
