package GameObject.Weapon;

import java.awt.Graphics2D;

import GameObject.Player.Player;
import TileMap.TileMap;

public class Bizon extends Weapon {

	public Bizon(TileMap tileMap) {
		super(64, 20, 10, 2.3, Weapon.FULLY_AUTOMATIC, Weapon.PISTOL, "/bizon.png", "/autoPistolSound.wav", tileMap);
		this.setFireRate(80);
	}

	
	public void moveWithPlayer(Player p) {
		if(getDirection() == FACING_RIGHT)
			setX(p.getX() + p.getWidth() - 20);
		else
			setX(p.getX() - this.getWidth() + 20);
		setY(p.getY() + p.getWidth() / 2);

	}

	
	public void shootBullet() {
		this.shootSingleBullet(new PistolBullet(this.getTileMap(), this.getPlayer().getTeam()));

	}

	
	public void init() {
		

	}

	
	public void draw(Graphics2D g) {
		this.drawImage(g);

	}

}
