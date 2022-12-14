package GameObject.Weapon;

import java.awt.Graphics2D;

import GameObject.Player.Player;
import GameObject.Player.Team;
import TileMap.TileMap;

public class PistolBullet extends Bullet{
	
	public PistolBullet(TileMap tileMap, Team team) {
		super(5, tileMap, 3, "/pistolBullet.png", team);
		
	}

	public void init() {
		
	}

	public void draw(Graphics2D g) {
		drawImage(g);
	}

	
	public void impact(Player p) {
		this.defaultImpact(p);
		
	}
}
