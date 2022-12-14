package GameObject.Weapon;

import java.awt.Graphics2D;

import GameObject.Player.Player;
import TileMap.TileMap;

public class Kalashnikova extends Weapon {

	public Kalashnikova(TileMap tileMap) {
		super(30, 25, 12, 3.5, Weapon.FULLY_AUTOMATIC, Weapon.RIFLE, "/kalashnikova.png", "/kalashnikovaSound.wav", tileMap);
		this.setFireRate(110);
	}

	
	public void moveWithPlayer(Player p) {
		if(this.getDirection() == FACING_RIGHT)
			this.setX(p.getX());
		else
			this.setX(p.getX() - 33);
		this.setY(p.getY() + 10);
	}

	
	public void init() {
		refill();

	}

	
	public void draw(Graphics2D g) {
		this.drawImage(g);

	}


	
	public void shootBullet() {
		this.shootSingleBullet(new RifleBullet(this.getTileMap(), this.getPlayer().getTeam()));
		
	}

}
