package GameObject.Weapon;

import java.awt.Graphics2D;

import GameObject.Player.Player;
import TileMap.TileMap;

public class Sniper extends Weapon {

	public Sniper(TileMap tileMap) {
		super(5, 1, 20, 6, Weapon.FULLY_AUTOMATIC, Weapon.SNIPER, "/sniper.png", "/sniper.wav", tileMap);
		this.setFireRate(1200);
	}

	
	public void moveWithPlayer(Player p) {
		if(this.getDirection() == FACING_RIGHT)
			this.setX(p.getX() - 4);
		else
			this.setX(p.getX() - 38);
		this.setY(p.getY() + 7);

	}

	
	public void shootBullet() {
		this.shootSingleBullet(new SniperBullet(this.getTileMap(), this.getPlayer().getTeam()));

	}

	
	public void init() {
		

	}

	
	public void draw(Graphics2D g) {
		this.drawImage(g);

	}

}
