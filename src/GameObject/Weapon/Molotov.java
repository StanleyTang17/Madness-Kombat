package GameObject.Weapon;

import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

import GameObject.Player.Player;
import TileMap.TileMap;

public class Molotov extends Weapon {
	
	private int damage;
	private int state;
	private long startTime;
	private int duration;
	private double launchXVel, launchYVel;
	private Image fireGif;
	
	private static final int HELD = 0;
	private static final int PULLED = 1;
	private static final int LAUNCHED = 2;
	private static final int EXPLODED = 3;
	
	public Molotov(TileMap tileMap) {
		super(-1, 1, 1, 0, Weapon.FULLY_AUTOMATIC, -1, "/molotov.png", "/molotov.wav", tileMap);
		state = HELD;
		damage = 1;
		launchXVel = 3;
		launchYVel = -2;
		duration = 10000;
		fireGif = new ImageIcon(getClass().getResource("/fire.gif")).getImage();
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
		this.setXVel(launchXVel * this.getDirection());
		this.setYVel(launchYVel);
		this.getPlayer().dropWeapon();
		this.setInAir(true);
	}
	
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
			if(state == LAUNCHED && (this.getXVel() == 0 || this.getYVel() == 0)) explode();
			if(state == EXPLODED)
			{
				burn();
				return;
			}
			move();
			if(getYVel() == 0) this.setEquipable(true);;
		}
		updateHitbox();
		checkTriggerPulled();
	}
	
	private void explode()
	{
		state = EXPLODED;
		this.playSoundEffect();
		this.setWidth(150);
		this.setX(this.getX() - this.getWidth() / 2);
		this.setHitBoxBySize();
		this.updateHitbox();
		this.setXVel(0);
		this.setYVel(0);
		this.setInAir(false);
		startTime = System.currentTimeMillis();
	}
	
	private void burn()
	{
		try
		{
			for(Player p : Player.PlayerList)
				if(p.collidesWith(this))
					p.takeDamage(damage);
		} catch(Exception e) {}
		if(System.currentTimeMillis() > startTime + duration) this.removeThis();
	}
	
	public void shootBullet() {
		

	}

	
	public void init() {
		

	}

	
	public void draw(Graphics2D g) {
		if(state != EXPLODED)
			this.drawImage(g);
		else
		{
			for(int i = 0; i < 5; i++)
			{
				g.drawImage(fireGif, (int)this.getX() + i * 30, (int)this.getY(), null);
			}
		}

	}

}
