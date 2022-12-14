package GameObject.Weapon;

import java.awt.Graphics2D;

import GameObject.Player.Player;
import GameObject.Player.Team;
import TileMap.TileMap;

public class RPG_Bullet extends Bullet {

	public RPG_Bullet(TileMap tileMap, Team team) {
		super(0, tileMap, 7, "/RPG_head.png", team);
		
	}

	
	public void init() {
		

	}
	
	@Override
	public void hitTerrain()
	{
		new Explosion(this.getX(), this.getY(), 40, 300);
		removeThis();
	}
	
	public void impact(Player p)
	{
		p.addExternalForce(50, getKnockback(), 0);
		new Explosion(this.getX(), this.getY(), 50, 300);
		this.removeThis();
	}
	
	
	public void draw(Graphics2D g) {
		this.drawImage(g);

	}

}
