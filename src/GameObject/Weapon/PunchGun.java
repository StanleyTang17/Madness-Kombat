package GameObject.Weapon;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import GameObject.Player.Player;
import GameState.LevelState.GameMode;
import GameState.LevelState.LevelState;
import Main.SoundEffect;
import TileMap.TileMap;

public class PunchGun extends Weapon {
	
	private BufferedImage closedImg;
	private BufferedImage openImg;
	private int damage;
	private double knockback;
	private boolean open;
	private SoundEffect punchSound;
	
	public PunchGun(TileMap tileMap) {
		super(-1, 1, 0, 1, Weapon.SEMI_AUTOMATIC, Weapon.PISTOL, "/punchGun.png", "/punchGun.wav", tileMap);
		try {
			closedImg = ImageIO.read(getClass().getResource("/punchGun.png"));
			openImg = ImageIO.read(getClass().getResource("/punchGunRelease.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		damage = 5;
		knockback = 20;
		open = false;
		punchSound = new SoundEffect(getClass().getResource("/punch.wav"));
	}
	
	@Override
	public void pullTrigger() 
	{ 
		if(!this.isTriggered())
		{
			this.setTriggerPulled(true);
			this.setImage(openImg);
			this.setSizeByImage();
			this.setHitBoxBySize();
			open = true;
			this.moveWithPlayer(getPlayer());
			this.updateHitbox();
			shootBullet();
		}
		else
		{
			this.setTriggerPulled(true);
		}
	}
	
	@Override
	public void releaseTrigger() 
	{ 
		this.setTriggerPulled(false); 
		this.setImage(closedImg);
		this.setSizeByImage();
		this.setHitBoxBySize();
		open = false;
		//this.setX(this.getX() - openImg.getWidth() * this.getDirection());
	}
	
	public void moveWithPlayer(Player p) {
		if(this.getDirection() == FACING_LEFT)
		{
			this.setX(p.getX() - p.getWidth());
			if(open) this.setX(this.getX() + (openImg.getWidth() - closedImg.getWidth()) * this.getDirection());
		}
		else
			this.setX(p.getX() + p.getWidth() - 10);
		this.setY(p.getY() + 15);
	}

	
	public void shootBullet() {
		try
		{
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
					punchSound.play();
				}
			}
		}catch(Exception e) {}
		this.playSoundEffect();
	}

	
	public void init() {
		

	}

	
	public void draw(Graphics2D g) {
		this.drawImage(g);
		
	}

}
