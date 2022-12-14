package GameObject.Weapon;

import java.awt.Graphics2D;

import GameObject.Player.Player;
import TileMap.TileMap;

public class SniperBullet extends Bullet{

	public SniperBullet(TileMap tileMap, GameObject.Player.Team team) {
		super(50, tileMap, 8, "/sniperBullet.png", team);
		
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
