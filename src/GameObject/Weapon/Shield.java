package GameObject.Weapon;

import java.awt.Graphics2D;
import java.util.ArrayList;

import GameObject.Player.Player;
import GameState.LevelState.GameMode;
import GameState.LevelState.LevelState;
import Main.SoundEffect;
import TileMap.TileMap;

public class Shield extends Weapon {
	
	private double damage;
	private double knockback;
	private double health;
	private SoundEffect bumpSound;
	
	public Shield(TileMap tileMap) {
		super(-1, 1, 1, 0, Weapon.SEMI_AUTOMATIC, Weapon.PISTOL, "/shield.png", "/shield.wav", tileMap);
		damage = 7.5;
		knockback = 10;
		health = 200;
		bumpSound = new SoundEffect(getClass().getResource("/bump.wav"));
	}

	
	public void moveWithPlayer(Player p) {
		hitTestBullet();
		if(this.getDirection() == FACING_LEFT)
		{
			this.setX(p.getX() - 10);
			if(this.isTriggered()) this.setX(this.getX() + 8 * this.getDirection());
		}
		else
		{
			this.setX(p.getX() + p.getWidth() - 10);
			if(this.isTriggered()) this.setX(this.getX() + 8 * this.getDirection());
		}
		this.setY(p.getY() + 5);

	}
	
	private void hitTestBullet()
	{
		ArrayList<Bullet> bullets = Bullet.BulletList;
		for(int i = 0; i < bullets.size(); i++)
		{
			Bullet b = bullets.get(i);
			if(b.collidesWith(this))
			{
				this.getPlayer().addExternalForce(50, b.getKnockback(), 0);
				this.takeDamage(b.getDamage());
				b.removeThis();
				i--;
				bumpSound.play();
			}
		}
	}
	
	public void takeDamage(double damage)
	{
		health -= damage;
		if(health <= 0)
		{
			this.getPlayer().dropWeapon();
			this.removeThis();
		}
	}
	
	public void shootBullet() {
		try
		{
			this.setX(getX() + 5 * this.getDirection());
			this.updateHitbox();
			for(Player p : Player.PlayerList)
			{
				boolean condition = false;
				if(LevelState.getGameMode() == GameMode.LAST_MAN_STANDING)
					condition = p.collidesWith(this) && p.getID() != this.getPlayer().getID();
				else if(LevelState.getGameMode() == GameMode.TEAM)
					condition = p.collidesWith(this) && p.getTeam() != this.getPlayer().getTeam();
				if(condition)
				{
					p.takeDamage(damage);
					p.addExternalForce(60, knockback * this.getDirection(), 0);
					bumpSound.play();
				}
			}
			this.setX(getX() - 5 * this.getDirection());
			this.updateHitbox();
		}catch(Exception e) {}
		//this.playSoundEffect();
	}

	
	public void init() {
		

	}

	
	public void draw(Graphics2D g) {
		this.drawImage(g);

	}

}
