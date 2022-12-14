package GameObject.Weapon;

import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import GameObject.GameObject;
import GameObject.Player.Player;
import GameObject.Player.Team;
import GameState.LevelState.GameMode;
import GameState.LevelState.LevelState;
import TileMap.TileMap;

public abstract class Bullet extends GameObject{
	
	private int damage;
	private double knockback;
	private boolean hitTerrain;
	public static ArrayList<Bullet> BulletList = new ArrayList<Bullet>();
	private Team team;
	
	public static void updateBullets()
	{
		if(!BulletList.isEmpty())
			for(int i = 0; i < BulletList.size(); i++)
			{
				Bullet b = BulletList.get(i);
				b.update();
				
				for(int j = 0; j < Player.PlayerList.size(); j++)
				{
					Player p = Player.PlayerList.get(j);
					boolean condition = false;
					if(LevelState.getGameMode() == GameMode.LAST_MAN_STANDING)
					{
						condition = b.collidesWith(p);
					}
					else if(LevelState.getGameMode() == GameMode.TEAM)
					{
						condition = b.collidesWith(p) && b.getTeam() != p.getTeam();
					}
					if(condition)
					{
						b.impact(p);
						i--;
						continue;
					}
				}
				
				if(b.hitsTerrain())
				{
					b.hitTerrain();
					i--;
					break;
				}
			}
	}
	
	public static void drawBullets(java.awt.Graphics2D g)
	{
		if(!BulletList.isEmpty())
			for(int i = 0; i < BulletList.size(); i++)
				BulletList.get(i).draw(g);
	}
	
	public Bullet(int dmg, TileMap tileMap, double knockback, String imageName, Team team) {
		damage = dmg;
		setTileMap(tileMap);
		hitTerrain = false;
		this.knockback = knockback;
		BulletList.add(this);
		this.team = team;
		try {
			setImage(ImageIO.read(getClass().getResource(imageName)));
			setSizeByImage();
			setHitBoxByImage();
		} catch (IOException e) {
			
		}
	}
	
	public void hitTerrain()
	{
		removeThis();
	}
	
	public void defaultImpact(Player p)
	{
		p.takeDamage(getDamage());
		p.addExternalForce(50, getKnockback(), 0);
		removeThis();
	}
	
	public abstract void impact(Player p);
	public int getDamage() { return damage; }
	public boolean hitsTerrain() { return hitTerrain; }
	public void removeThis() { 
		int index = BulletList.indexOf(this);
		if(index != -1)
			BulletList.remove(index);
	}
	
	@Override
	public void update()
	{
		setX(getX() + getXVel());
		setY(getY() + getYVel());
		updateHitbox();
		if(this.checkOutOfBound())
		{
			removeThis();
			return;
		}
		terrainCollision();
		if(this.getXVel() == 0 || (!this.inAir() && this.getYVel() == 0))
		{
			hitTerrain = true;
		}
	}
	
	public double getKnockback() { return knockback * getDirection(); }
	public Team getTeam() { return team; }
}
