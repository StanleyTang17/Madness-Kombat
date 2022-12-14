package GameObject.Weapon;

import java.awt.Graphics2D;

import GameObject.Player.Player;
import TileMap.TileMap;

public class M14 extends Weapon {

	public M14(TileMap tileMap) {
		super(20, 5, 13, 4, Weapon.SEMI_AUTOMATIC, Weapon.RIFLE, "/M14.png", "/kalashnikovaSound.wav", tileMap);
		
	}

	
	public void moveWithPlayer(Player p) {
		if(this.getDirection() == FACING_RIGHT)
			this.setX(p.getX() - 4);
		else
			this.setX(p.getX() - 25);
		this.setY(p.getY() + 10);

	}

	
	public void shootBullet() {
		this.shootSingleBullet(new M14Bullet(this.getTileMap(), this.getPlayer().getTeam()));

	}

	
	public void init() {
		

	}

	
	public void draw(Graphics2D g) {
		this.drawImage(g);

	}

}
