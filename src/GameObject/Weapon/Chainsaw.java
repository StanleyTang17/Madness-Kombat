package GameObject.Weapon;

import java.awt.Graphics2D;

import GameObject.Player.Player;
import GameState.LevelState.GameMode;
import GameState.LevelState.LevelState;
import TileMap.TileMap;

public class Chainsaw extends Weapon {
	
	private int damage;
	private boolean swing;
	private long lastSwinged;
	
	public Chainsaw(TileMap tileMap) {
		super(500, 0, 0, 0, Weapon.SEMI_AUTOMATIC, Weapon.MELEE, "/chainsaw.png", "/chainsaw.wav", tileMap);
		this.setFireRate(20);
		damage = 3;
		swing = false;
	}

	
	public void moveWithPlayer(Player p) {
		if(this.getDirection() == FACING_RIGHT)
			this.setX(p.getX() + p.getWidth() - 15);
		else
			this.setX(p.getX() - this.getWidth() + 15);
		this.setY(p.getY() + 25);
	}

	@Override
	public void pullTrigger()
	{
		shootBullet();
	}
	
	public void shootBullet() {
		if(this.getAmmoCount() <= 0) return;
		this.setAmmoCount(this.getAmmoCount() - 1);
		if(!swing)
		{
			swing = true;
			lastSwinged = System.currentTimeMillis();
			try
			{
				for(Player p : Player.PlayerList)
				{
					boolean condition = false;
					if(LevelState.getGameMode() == GameMode.LAST_MAN_STANDING)
						condition = p.getID() != this.getPlayer().getID() && p.collidesWith(this);
					else if(LevelState.getGameMode() == GameMode.TEAM)
						condition = p.collidesWith(this) && this.getPlayer().getTeam() != p.getTeam();
					if(condition)
					{
						p.takeDamage(damage);
						if(p.hasWeapon() && p.getWeapon().getClass() == Shield.class)
						{
							Shield shield = (Shield) p.getWeapon();
							shield.takeDamage(damage);
						}
					}
				}
			} catch(Exception e)
			{
				
			}
		}
		this.playSoundEffect();
	}

	
	public void init() {
		swing = false;

	}

	
	public void draw(Graphics2D g) {
		this.drawImage(g);
		
	}
	
	@Override
	public void update()
	{
		updateMovement();
		this.updateHitbox();
		updateSwing();
		
	}
	
	private void updateSwing()
	{
		if(swing)
		{
			if(System.currentTimeMillis() > lastSwinged + this.getFireRate())
			{
				swing = false;
				return;
			}
			this.setX(getX() + 10 * this.getDirection());
		}
		
	}
	
	private void updateMovement()
	{
		if(this.isEquipped()) 
		{ 
			moveWithPlayer(this.getPlayer());
		} 
		else 
		{
			if(checkOutOfBound())
			{
				respawn();
				return;
			}
			terrainCollision();
			move();
			if(getYVel() == 0) this.setEquipable(true);
		}
	}
}
