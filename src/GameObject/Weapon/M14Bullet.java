package GameObject.Weapon;

import java.awt.Graphics2D;

import GameObject.Player.Player;
import GameObject.Player.Team;
import TileMap.TileMap;

public class M14Bullet extends Bullet {

	public M14Bullet(TileMap tileMap, Team team) {
		super(10, tileMap, 6, "/rifleBullet.png", team);
		// TODO Auto-generated constructor stub
	}

	
	public void impact(Player p) {
		this.defaultImpact(p);

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Graphics2D g) {
		this.drawImage(g);

	}

}
