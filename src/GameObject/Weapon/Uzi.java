package GameObject.Weapon;

import java.awt.Graphics2D;

import GameObject.Player.Player;
import TileMap.TileMap;

public class Uzi extends Weapon{
	
	public Uzi(TileMap tileMap) {
		super(23, 23, 11, 1.7, Weapon.FULLY_AUTOMATIC, Weapon.PISTOL,"/auto-pistol.png", "/autoPistolSound.wav", tileMap);
		setFireRate(40);
		
	}
	
	public void moveWithPlayer(Player p) {
		if(getDirection() == FACING_RIGHT)
			setX(p.getX() + p.getWidth() - 7);
		else
			setX(p.getX() - this.getWidth() + 6);
		setY(p.getY() + p.getWidth() / 2 - 9);
		
	}

	
	public void init() {
		refill();
		
	}
	
	public void draw(Graphics2D g) {
		drawImage(g);
		
	}

	
	public void shootBullet() {
		this.shootSingleBullet(new PistolBullet(this.getTileMap(), this.getPlayer().getTeam()));
		
	}

}
