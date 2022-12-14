package GameObject.Weapon;


import java.awt.Graphics2D;

import GameObject.Player.Player;
import TileMap.TileMap;

public class ShotgunPellet extends Bullet {

	public ShotgunPellet(TileMap tileMap, GameObject.Player.Team team) {
		super(3, tileMap, 1.6, "/shotgunPellet.png", team);
		this.setWidth(4);
		this.setHeight(4);
		this.setHitBoxBySize();
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
