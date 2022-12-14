package GameObject.Weapon;

import java.awt.Graphics2D;

import GameObject.Player.Player;
import TileMap.TileMap;

public class Pistol extends Weapon{
	
	public Pistol(TileMap tileMap) {
		super(13, 10, 8, 1, Weapon.SEMI_AUTOMATIC, Weapon.PISTOL, "/pistol.png", "/pistolSound.wav", tileMap);
	}
	
	public void moveWithPlayer(Player p) {
		setX(p.getX() + p.getWidth() * getDirection() - 3);
		setY(p.getY() + p.getWidth() / 2 - 10);
	}

	
	public void init() {
		this.refill();
		
	}

	
	public void draw(Graphics2D g) {
		this.drawImage(g);
	}

	
	public void shootBullet() {
		this.shootSingleBullet(new PistolBullet(this.getTileMap(), this.getPlayer().getTeam()));
		this.setAmmoCount(this.getAmmoCount() + 1);
	}
	
	
}
