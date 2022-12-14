package GameObject.Weapon;

import java.awt.Point;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import GameObject.GameObject;
import GameObject.Player.Player;
import Main.SoundEffect;
import TileMap.TileMap;

public abstract class Weapon extends GameObject {
	
	private boolean triggerPulled;
	private boolean equipped;
	private boolean equipable;
	private Player player;
	private int ammoCount;
	private int maxAmmoCount;
	private int mode;
	private int type;
	private double recoil;
	private long lastTimeFiring;
	private double accuracy;
	private double bulletSpeed;
	private double fireRate;
	private long recoilTime;
	private long despawnTime;
	private boolean despawnable;
	private boolean mute;
	private SoundEffect sound;
	
	private static ArrayList<Point> SpawnPoints = new ArrayList<Point>();
	public static ArrayList<Weapon> WeaponList = new ArrayList<Weapon>();
	
	public static final int SEMI_AUTOMATIC = 0;
	public static final int FULLY_AUTOMATIC = 1;
	
	public static final int PISTOL = 0;
	public static final int RIFLE = 1;
	public static final int SHOTGUN = 2;
	public static final int SNIPER = 3;
	public static final int MELEE = 4;
	public static final int RPG = 5;
	
	public static void updateWeapons()
	{
		if(!WeaponList.isEmpty())
			for(int i = 0; i < WeaponList.size(); i++)
			{
				WeaponList.get(i).update();
			}
		Explosion.updateExplosions();
	}
	
	public static void drawWeapons(java.awt.Graphics2D g)
	{
		if(!WeaponList.isEmpty())
			for(int i = 0; i < WeaponList.size(); i++)
				WeaponList.get(i).draw(g);
		Explosion.drawExplosions(g);
	}
	
	public Weapon(int maxAmmoCount, double accuracy, double bulletSpeed, double recoil, int mode, int type, 
			String imageFileName, String soundFileName, TileMap tileMap)
	{
		try {
			this.setImage(ImageIO.read(getClass().getResource(imageFileName)));
			this.setSizeByImage();
			this.setHitBoxByImage();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mute = false;
		this.setSoundEffect(getClass().getResource(soundFileName));
		setMaxAmmoCount(maxAmmoCount);
		refill();
		this.accuracy = accuracy;
		this.bulletSpeed = bulletSpeed;
		this.mode = mode;
		this.recoil = recoil;
		this.type = type;
		setTileMap(tileMap);
		equipable = true;
		WeaponList.add(this);
		setXVel(0);
		setYVel(0);
		setDirection(FACING_RIGHT);
		if(mode == SEMI_AUTOMATIC)
		{
			setTriggerPulled(true);
		}
		else
		{
			setTriggerPulled(false);
		}
		lastTimeFiring = System.currentTimeMillis();
		recoilTime = System.currentTimeMillis();
	}
	
	public abstract void moveWithPlayer(Player p);
	public abstract void shootBullet();
	
	public void applyRecoil()
	{
		player.addExternalForce(20, -this.getDirection() * this.getRecoil(), 0);
	}
	
	public void respawn() {
		if(SpawnPoints != null)
		{
			this.spawnRandom(SpawnPoints);
			this.refill();
		}
	}
	public void shootSingleBullet(Bullet b)
	{
		if(ammoCount <= 0) return;
		int dir = this.getDirection();
		double x = 0;
		double y = getY();
		double accuracy = this.getAccuracy();
		double angle = (Math.random() * accuracy - accuracy / 2) / 180 * Math.PI;
		double speed = this.getBulletSpeed();
		double xVel = speed * dir * Math.cos(angle);
		double yVel = speed * Math.sin(angle);
		
		if(dir == FACING_LEFT)
		{
			x = getX() - b.getWidth();
		}
		else if(dir == FACING_RIGHT)
		{
			x = getX() + getWidth();
		}
		
		b.setXVel(xVel);
		b.setYVel(yVel);
		b.setDirection(dir);
		b.setX(x);
		b.setY(y);
		lastTimeFiring = System.currentTimeMillis();
		ammoCount--;
		recoilTime = System.currentTimeMillis() + (int)(recoil * 10);
		if(sound != null && !mute) sound.play();
		this.applyRecoil();
	}
	
	public void pullTrigger() 
	{ 
		if(mode == SEMI_AUTOMATIC)
		{
			if(!this.isTriggered())
			{
				triggerPulled = true;
				shootBullet();
			}
		}
		else
		{
			triggerPulled = true;
		}
	}
	
	@Override
	public void update() {
		this.defaultUpdate();
	}
	
	public void defaultUpdate()
	{
		if(this.isEquipped()) 
		{ 
			moveWithPlayer(this.getPlayer());
		} 
		else 
		{
			checkDespawn();
			if(this.getY() > 700)
			{
				respawn();
				return;
			}
			terrainCollision();
			move();
			if(getYVel() == 0) equipable = true;
		}
		updateHitbox();
		checkTriggerPulled();
		if(System.currentTimeMillis() < recoilTime)
		{
			setX(getX() - this.getDirection() * recoil);
		}
	}
	
	private void checkDespawn()
	{
		if(despawnable && System.currentTimeMillis() > despawnTime)
			removeThis();
	}
	
	public void equip(Player p) {
		if(equipable)
		{
			player = p; 
			equipped = true; 
			setDirection(p.getDirection());
			equipable = false;
		}
	}
	
	public void mute() { mute = true; }
	public void unmute() { mute = false; }
	public void setEquipable(boolean equipable) { this.equipable = equipable; }
	public void releaseTrigger() { triggerPulled = false; }
	public void setTriggerPulled(boolean t) { triggerPulled = t; }
	public boolean isTriggered() { return triggerPulled; }
	public Player getPlayer() { return player; }
	public void setPlayer(Player p) { player = p; }
	public void refill() { ammoCount = maxAmmoCount; }
	public int getAmmoCount() { return ammoCount; }
	public void setAmmoCount(int count) { ammoCount = count; }
	public int getMaxAmmoCount() { return maxAmmoCount; }
	public void setMaxAmmoCount(int count) { maxAmmoCount = count; }
	public double getAccuracy() { return accuracy; }
	public void setAccuracy(double a) { accuracy = a; }
	public boolean isEquipped() { return equipped; }
	public void setEquipped(boolean b) { 
		equipped = b; 
		if(!equipped)
			if(ammoCount == 0)
			{
				despawnTime = System.currentTimeMillis() + 15000;
				despawnable = true;
			}
		else
			despawnable = false;
	}
	public double getBulletSpeed() { return bulletSpeed; }
	public void setBulletSpeed(double speed) { bulletSpeed = speed; }
	public void removeThis() { sound.stop(); WeaponList.remove(WeaponList.indexOf(this)); }
	public static void setSpawnPoints(ArrayList<Point> spawnPoints) { SpawnPoints = spawnPoints; }
	public static ArrayList<Point> getSpawnPoints() { return SpawnPoints; }
	public double getFireRate() { return fireRate; }
	public void setFireRate(double fps) { fireRate = fps; }
	public double getRecoil() { return recoil; }
	public void setRecoil(double recoil) { this.recoil = recoil; }
	public int getType() { return type; }
	public void setSoundEffect(URL url) { sound = new SoundEffect(url); }
	public void playSoundEffect() { sound.play(); }
	
	public void checkTriggerPulled()
	{
		if(mode == FULLY_AUTOMATIC && isTriggered())
		{
			long curTime = System.currentTimeMillis();
			if(curTime - lastTimeFiring >= fireRate)
			{
				lastTimeFiring = curTime;
				shootBullet();
			}
		}
	}
}
