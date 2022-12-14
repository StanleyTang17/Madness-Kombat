package GameObject.Weapon;

import java.awt.Graphics2D;

import GameObject.Player.Player;
import TileMap.TileMap;

public class RifleBullet extends Bullet {

	public RifleBullet(TileMap tileMap, GameObject.Player.Team team) {
		super(8, tileMap, 5, "/rifleBullet.png", team);
	}

	public void init() {
		

	}
	
	public void draw(Graphics2D g) {
		this.drawImage(g);

	}

	
	public void impact(Player p) {
		this.defaultImpact(p);
		
	}

}
