package GameObject.Weapon;

import java.awt.Graphics2D;

import GameObject.Player.Player;
import TileMap.TileMap;

public class Shotgun extends Weapon {
	
	private int pelletsPerShot;
	
	public Shotgun(TileMap tileMap) {
		super(64, 40, 10, 1.5, Weapon.FULLY_AUTOMATIC, Weapon.SHOTGUN, "/shotgun.png", "/shotgun.wav", tileMap);
		this.setFireRate(1000);
		pelletsPerShot = 10;
		this.mute();
	}

	
	public void moveWithPlayer(Player p) {
		if(this.getDirection() == FACING_RIGHT)
			setX(p.getX() - 3);
		else
			setX(p.getX() - 25);
		setY(p.getY() + 10);

	}

	
	public void init() {
		

	}

	
	public void draw(Graphics2D g) {
		this.drawImage(g);

	}


	
	public void shootBullet() {
		if(this.getAmmoCount() > 0)
			this.playSoundEffect();
		for(int i = 0; i < pelletsPerShot; i++)
		{
			this.shootSingleBullet(new ShotgunPellet(this.getTileMap(), this.getPlayer().getTeam()));
		}
	}

}
